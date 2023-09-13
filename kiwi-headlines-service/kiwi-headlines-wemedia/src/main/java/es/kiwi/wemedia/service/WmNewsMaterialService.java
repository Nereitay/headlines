package es.kiwi.wemedia.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.dtos.WmNewsPageReqDto;

import java.util.List;

public interface WmNewsMaterialService {

    /**
     * 文章与素材的关联关系批量保存
     * @param materialIds
     * @param newsId
     * @param type
     */
    void saveRelations(List<Integer> materialIds, Integer newsId, Short type);

    void deleteAllFromNewsId(Integer id);
}
