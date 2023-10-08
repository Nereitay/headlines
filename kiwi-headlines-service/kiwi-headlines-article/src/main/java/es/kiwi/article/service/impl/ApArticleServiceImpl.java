package es.kiwi.article.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import es.kiwi.article.repository.ApArticleConfigRepository;
import es.kiwi.article.repository.ApArticleContentRepository;
import es.kiwi.article.repository.ApArticleRepository;
import es.kiwi.article.service.ApArticleService;
import es.kiwi.article.service.ArticleFreemarkerService;
import es.kiwi.common.constants.ArticleConstants;
import es.kiwi.common.constants.BehaviorConstants;
import es.kiwi.common.redis.CacheService;
import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.article.dtos.ArticleHomeDto;
import es.kiwi.model.article.dtos.ArticleInfoDto;
import es.kiwi.model.article.mapstruct.mappers.ApArticleMapper;
import es.kiwi.model.article.msg.ArticleVisitStreamMsg;
import es.kiwi.model.article.pojos.*;
import es.kiwi.model.article.vos.HotArticleVo;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.user.pojos.ApUser;
import es.kiwi.utils.common.UpdateUtil;
import es.kiwi.utils.thread.AppThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service("apArticleService")
@Transactional
@Slf4j
public class ApArticleServiceImpl implements ApArticleService {

    @Autowired
    private ApArticleRepository apArticleRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private ApArticleConfigRepository apArticleConfigRepository;

    @Autowired
    private ApArticleContentRepository apArticleContentRepository;

    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;

    @Autowired
    private CacheService cacheService;

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

    /**
     * 加载文章列表
     * @param dto
     * @param type
     * @param firstPage true 首页 false 非首页
     * @return
     */
    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage) {
        if (firstPage) {
            String jsonStr = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
            if (StringUtils.isNotBlank(jsonStr)) {
                try {
                    List<HotArticleVo> hotArticleVoList = new ObjectMapper().readValue(jsonStr, new TypeReference<List<HotArticleVo>>() {});
                    return ResponseResult.okResult(hotArticleVoList);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    log.error("load2() JsonProcessingException in ApArticleServiceImpl");
                }
            }
        }
        return load(dto, type);
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

    /**
     * 保存app端相关文章
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        /*测试熔断
        -- 注意 WmNewsAutoScanServiceImpl调用此方法出现熔断后抛异常，但此方法保存的内容不会回滚
            如何解决？加了return。。
        */
        /*try {
            Thread.sleep(3000);
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //1.检查参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle = ApArticleMapper.INSTANCE.dtoToPojo(dto);
        //2.判断是否存在id
        if (apArticle.getId() == null) {
            //2.1 不存在id  保存  文章  文章配置  文章内容
            //保存文章
            apArticle = apArticleRepository.saveAndFlush(apArticle);
            //保存配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfig.setArticleId(apArticle.getId());
            apArticleConfigRepository.save(apArticleConfig);
            //保存 文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentRepository.save(apArticleContent);
        } else {
            //2.2 存在id   修改  文章  文章内容
            //修改  文章
            /* 只更新不为null的字段*/
            Optional<ApArticle> apArticleOpt = apArticleRepository.findById(apArticle.getId());
            if (apArticleOpt.isPresent()) {
                ApArticle apArticleDb = apArticleOpt.get();
                UpdateUtil.copyNullProperties(apArticleOpt, apArticleDb);
                apArticleRepository.save(apArticleDb);
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "文章Id不存在：" + apArticle.getId());
            }
            //修改文章内容
            ApArticleContent apArticleContent = apArticleContentRepository.findByArticleId(dto.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentRepository.save(apArticleContent);
        }

        // 异步调用 生成静态文件上传到minio中
        articleFreemarkerService.buildArticleToMinIO(apArticle, dto.getContent());

        //3.结果返回  文章的id
        return ResponseResult.okResult(apArticle.getId());
    }

    /**
     * 加载文章详情 数据回显
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {
        //0.检查参数
        if (dto == null || dto.getArticleId() == null || dto.getAuthorId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //{ "isfollow": true, "islike": true,"isunlike": false,"iscollection": true }
        boolean isfollow = false, islike = false, isunlike = false, iscollection = false;

        ApUser user = AppThreadLocalUtils.getUser();
        if(user != null){
            //喜欢行为
            String likeBehaviorJson = (String) cacheService.hGet(
                    BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString());
            if(StringUtils.isNotBlank(likeBehaviorJson)){
                islike = true;
            }
            //不喜欢的行为
            String unLikeBehaviorJson = (String) cacheService.hGet(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString());
            if(StringUtils.isNotBlank(unLikeBehaviorJson)){
                isunlike = true;
            }
            //是否收藏
            String collctionJson = (String) cacheService.hGet(BehaviorConstants.COLLECTION_BEHAVIOR+user.getId(),dto.getArticleId().toString());
            if(StringUtils.isNotBlank(collctionJson)){
                iscollection = true;
            }

            //是否关注
            Double score = cacheService.zScore(BehaviorConstants.APUSER_FOLLOW_RELATION + user.getId(), dto.getAuthorId().toString());
            System.out.println(score);
            if(score != null){
                isfollow = true;
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isfollow", isfollow);
        resultMap.put("islike", islike);
        resultMap.put("isunlike", isunlike);
        resultMap.put("iscollection", iscollection);

        return ResponseResult.okResult(resultMap);
    }

    @Override
    public List<ApArticle> findArticleListByLast5Days(Date dayParam) {
        QApArticle apArticle = QApArticle.apArticle;
        QApArticleConfig apArticleConfig = QApArticleConfig.apArticleConfig;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(apArticleConfig.isDelete.eq(false))
                .and(apArticleConfig.isDown.eq(false))
                .and(apArticle.publishTime.goe(dayParam));

        return jpaQueryFactory.selectFrom(apArticle).leftJoin(apArticleConfig)
                .on(apArticle.id.eq(apArticleConfig.articleId))
                .where(booleanBuilder).fetch();
    }

    /**
     * 更新文章的分值 同时更新缓存中的热点文章数据
     *
     * @param msg
     */
    @Override
    public void updateScore(ArticleVisitStreamMsg msg) {
        //1.更新文章的阅读、点赞、收藏、评论的数量
        ApArticle apArticle = updateArticle(msg);

        //2.计算文章的分值
        Integer score = computeScore(apArticle);
        score = score * 3;

        //3.替换当前文章对应频道的热点数据
        replaceDataToRedis(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + apArticle.getChannelId(), apArticle, score);

        //4.替换推荐对应的热点数据
        replaceDataToRedis(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG, apArticle, score);
    }

    /**
     * 替换数据并且存入到redis
     * @param key
     * @param apArticle
     * @param score
     */
    private void replaceDataToRedis(String key, ApArticle apArticle, Integer score) {
        String articleListStr = cacheService.get(key);
        if (StringUtils.isNotBlank(articleListStr)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<HotArticleVo> hotArticleVoList = objectMapper.readValue(articleListStr, new TypeReference<List<HotArticleVo>>() {
                });
                boolean flag = true;
                //如果缓存中存在该文章，只更新分值
                for (HotArticleVo hotArticleVo : hotArticleVoList) {
                    if (hotArticleVo.getId().equals(apArticle.getId())) {
                        hotArticleVo.setScore(score);
                        flag = false;
                        break;
                    }
                }

                //如果缓存中不存在，查询缓存中分值最小的一条数据，进行分值的比较，如果当前文章的分值大于缓存中的数据，就替换
                if (flag) {
                    if (hotArticleVoList.size() >= 30) {
                        hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
                        HotArticleVo lastHot = hotArticleVoList.get(hotArticleVoList.size() - 1);
                        if (lastHot.getScore() < score) {
                            hotArticleVoList.remove(lastHot);
                            HotArticleVo hot = ApArticleMapper.INSTANCE.pojoToHotArticleVo(apArticle);
                            hot.setScore(score);
                            hotArticleVoList.add(hot);
                        }
                    } else {
                        HotArticleVo hot = ApArticleMapper.INSTANCE.pojoToHotArticleVo(apArticle);
                        hot.setScore(score);
                        hotArticleVoList.add(hot);
                    }
                }
                //缓存到redis
                hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
                cacheService.set(key, objectMapper.writeValueAsString(hotArticleVoList));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 更新文章行为数量
     * @param msg
     * @return
     */
    private ApArticle updateArticle(ArticleVisitStreamMsg msg) {
        AtomicReference<ApArticle> article = new AtomicReference<>();
        Optional<ApArticle> apArticleOptional = apArticleRepository.findById(msg.getArticleId());
        apArticleOptional.ifPresent(apArticle -> {
            apArticle.setCollection(apArticle.getCollection() == null ? msg.getCollect() : apArticle.getCollection() + msg.getCollect());
            apArticle.setComment(apArticle.getComment() == null ? msg.getCollect() : apArticle.getComment() + msg.getComment());
            apArticle.setLikes(apArticle.getLikes() == null ? msg.getLike() : apArticle.getLikes() + msg.getLike());
            apArticle.setViews(apArticle.getViews() == null ? msg.getView() : apArticle.getViews() + msg.getView());
            apArticleRepository.save(apArticle);
            article.set(apArticle);
        });

        return article.get();
    }

    /**
     * 计算文章的具体分值
     * @param apArticle
     * @return
     */
    private Integer computeScore(ApArticle apArticle) {
        int score = 0;
        if(apArticle.getLikes() != null){
            score += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if(apArticle.getViews() != null){
            score += apArticle.getViews();
        }
        if(apArticle.getComment() != null){
            score += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if(apArticle.getCollection() != null){
            score += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }

        return score;
    }
}
