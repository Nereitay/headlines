package es.kiwi.wemedia.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.dtos.NewsAuthDto;
import es.kiwi.model.wemedia.dtos.WmNewsDto;
import es.kiwi.model.wemedia.dtos.WmNewsPageReqDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface WmNewsService {

    /**
     * 条件查询文章列表
     * @param dto
     * @return
     */
    ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * 发布文章或保存草稿
     * @param dto
     * @return
     */
    ResponseResult submitNews(WmNewsDto dto);

    /**
     * 文章上下架
     * @param dto
     * @return
     */
    ResponseResult downOrUp(WmNewsDto dto);

    /**
     * 查询文章列表
     * @param dto
     * @return
     */
    ResponseResult findList(NewsAuthDto dto);

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    ResponseResult findWmNewsVo(Integer id);
    /**
     * 文章审核，修改状态
     * @param status  2  审核失败  4 审核成功
     * @param dto
     * @return
     */
    ResponseResult updateStatus(Short status, NewsAuthDto dto);
}
