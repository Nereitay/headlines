package es.kiwi.wemedia.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.pojos.WmNews;

public interface WmNewsAutoScanService {

    /**
     * 自媒体文章审核
     * @param id 自媒体文章id
     */
    void autoScanWmNews(Integer id);

    /**
     * 保存app文章数据
     * @param wmNews
     * @return
     */
    ResponseResult saveAppArticle(WmNews wmNews);
}
