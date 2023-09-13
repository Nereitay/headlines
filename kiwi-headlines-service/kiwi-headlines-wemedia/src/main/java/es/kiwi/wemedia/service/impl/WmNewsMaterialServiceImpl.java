package es.kiwi.wemedia.service.impl;

import es.kiwi.model.wemedia.pojos.WmNewsMaterial;
import es.kiwi.wemedia.repository.WmNewsMaterialRepository;
import es.kiwi.wemedia.service.WmNewsMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class WmNewsMaterialServiceImpl implements WmNewsMaterialService {
    @Autowired
    private WmNewsMaterialRepository wmNewsMaterialRepository;

    @Override
    public void saveRelations(List<Integer> materialIds, Integer newsId, Short type) {
        for (int i = 0; i < materialIds.size(); i++) {
            wmNewsMaterialRepository.saveRelation(materialIds.get(i), newsId, type, (short) i);
        }
    }

    @Override
    public void deleteAllFromNewsId(Integer newsId) {
        List<WmNewsMaterial> wmNewsMaterialList = wmNewsMaterialRepository.findByNewsId(newsId);
        wmNewsMaterialRepository.deleteAll(wmNewsMaterialList);
    }
}
