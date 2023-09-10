package es.kiwi.article.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import es.kiwi.article.repository.ApArticleRepository;
import es.kiwi.article.service.ApArticleService;
import es.kiwi.common.constants.ArticleConstants;
import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.article.pojos.QApArticle;
import es.kiwi.model.article.pojos.QApArticleConfig;
import es.kiwi.model.common.dtos.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("apArticleService")
public class ApArticleServiceImpl implements ApArticleService {

    @Autowired
    private ApArticleRepository apArticleRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static final short MAX_PAGE_SIZE = 50;

    /**
     * 加载文章列表
     *
     * @param dto
     * @param type 1 加载更多 2 加载最新
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        // 1.校验参数
        // 分页条数的校验
        Integer size = dto.getSize();
        if (size == null || size == 0) {
            size = 10;
        }

        // 分页的值不超过50
        size = Math.min(size, MAX_PAGE_SIZE);
        dto.setSize(size);

        // 校验参数 --> type
        if (!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstants.LOADTYPE_LOAD_LATEST)) {
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        // 频道参数校验
        if (StringUtils.isBlank(dto.getTag())) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        // 时间校验
        if (dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(new Date());
        }

        // 2 查询
        List<ApArticle> apArticles = loadArticleList(dto, type);
        // 3 结果返回
        return ResponseResult.okResult(apArticles);
    }

    @Override
    public List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type) {

        QApArticle apArticle = QApArticle.apArticle;
        QApArticleConfig apArticleConfig = QApArticleConfig.apArticleConfig;

        // 初始化组装条件(类似where 1=1)
        Predicate predicate = apArticle.isNotNull().or(apArticle.isNull());
        // load more
        predicate = type != null && type == 1 ? ExpressionUtils.and(predicate, apArticle.publishTime.lt(dto.getMinBehotTime())) : predicate;
        // load latest
        predicate = type != null && type == 2 ? ExpressionUtils.and(predicate, apArticle.publishTime.gt(dto.getMaxBehotTime())) : predicate;
        predicate = !dto.getTag().equals(ArticleConstants.DEFAULT_TAG) ? ExpressionUtils.and(predicate, apArticle.channelId.eq(Integer.valueOf(dto.getTag()))) : predicate;

        return jpaQueryFactory.selectFrom(apArticle)
                .leftJoin(apArticleConfig)
                .on(apArticle.id.eq(apArticleConfig.articleId))
                .where(apArticleConfig.isDelete.eq(false).and(apArticleConfig.isDown.eq(false)), predicate)
                .orderBy(apArticle.publishTime.desc())
                .limit(dto.getSize())
                .fetch();

    }
}
