package es.kiwi.article.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.kiwi.apis.wemedia.IWemediaClient;
import es.kiwi.article.service.ApArticleService;
import es.kiwi.article.service.HotArticleService;
import es.kiwi.common.constants.ArticleConstants;
import es.kiwi.common.redis.CacheService;
import es.kiwi.model.article.mapstruct.mappers.ApArticleMapper;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.article.vos.HotArticleVo;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class HotArticleServiceImpl implements HotArticleService {

    @Autowired
    private ApArticleService apArticleService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private IWemediaClient wemediaClient;


    /**
     * 计算热点文章
     */
    @Override
    public void computeHotArticle() {
        //1. 查询前5天的文章数据
        Date dateParam = DateTime.now().minusDays(5).toDate();
        List<ApArticle> apArticleList = apArticleService.findArticleListByLast5Days(dateParam);

        //2. 计算文章的分值
        List<HotArticleVo> hotArticleVoList = computeHotArticle(apArticleList);

        //3. 为每个频道缓存30条分值较高的文章
        cacheTagToRedis(hotArticleVoList);
    }

    /**
     * 为每个频道缓存30条分值较高的文章
     * @param hotArticleVoList
     */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleVoList) {
        // 为每个频道缓存30条分值较高的文章
        ResponseResult responseResult = wemediaClient.findAll();
        if (responseResult.getCode().equals(200)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String channelsJson = objectMapper.writeValueAsString(responseResult.getData());
                List<WmChannel> wmChannelList = objectMapper.readValue(channelsJson, new TypeReference<List<WmChannel>>() {});
                // 检索出每个频道的文章
                for (WmChannel wmChannel : wmChannelList) {
                    List<HotArticleVo> hotArticleVosForEachChannel = hotArticleVoList.stream().filter(h -> h.getChannelId().equals(wmChannel.getId())).collect(Collectors.toList());
                    //给文章进行排序，取30条分值较高的文章存入redis  key：频道id   value：30条分值较高的文章
                    sortAndCache(hotArticleVosForEachChannel, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + wmChannel.getId());
                }
                // 设置推荐数据
                sortAndCache(hotArticleVoList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("cacheTagToRedis() JsonProcessingException in HotArticleServiceImpl");
            }

        }

        // 设置推荐数据
    }

    /**
     * 排序并且缓存数据
     * @param hotArticleVos
     * @param key
     */
    private void sortAndCache(List<HotArticleVo> hotArticleVos, String key) {
        hotArticleVos = hotArticleVos.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
        if (hotArticleVos.size() > 30) {
            hotArticleVos = hotArticleVos.subList(0, 30);
        }
        try {
            cacheService.set(key, new ObjectMapper().writeValueAsString(hotArticleVos));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("sortAndCache() JsonProcessingException in HotArticleServiceImpl");
        }
    }

    /**
     * 计算文章的分值
     * @param apArticleList
     * @return
     */
    private List<HotArticleVo> computeHotArticle(List<ApArticle> apArticleList) {
        List<HotArticleVo> hotArticleVoList = new ArrayList<>();

        for (ApArticle apArticle : apArticleList) {
            HotArticleVo hotArticleVo = ApArticleMapper.INSTANCE.pojoToHotArticleVo(apArticle);
            int score = computeScore(apArticle);
            hotArticleVo.setScore(score);
            hotArticleVoList.add(hotArticleVo);
        }

        return hotArticleVoList;
    }

    /**
     * 计算文章具体分值
     * @param apArticle
     * @return
     */
    private int computeScore(ApArticle apArticle) {
        int score = 0;
        if (apArticle.getLikes() != null) {
            score += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if (apArticle.getViews() != null) {
            score += apArticle.getViews();
        }
        if (apArticle.getComment() != null) {
            score += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if (apArticle.getCollection() != null) {
            score += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }

        return score;
    }
}
