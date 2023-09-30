package es.kiwi.article.service.impl;

import es.kiwi.article.repository.ApArticleConfigRepository;
import es.kiwi.article.service.ApArticleConfigService;
import es.kiwi.model.article.pojos.ApArticleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@Transactional
public class ApArticleConfigServiceImpl implements ApArticleConfigService {

    @Autowired
    private ApArticleConfigRepository apArticleConfigRepository;

    /**
     * 修改文章配置
     *
     * @param map
     */
    @Override
    public void updateByMap(Map map) {
        // 0 下架 1 上架
        Object enable = map.get("enable");
        boolean isDown = true;
        if (enable.equals(1)) {
            isDown = false;
        }
        // 修改文章
        ApArticleConfig apArticleConfig = apArticleConfigRepository.findByArticleId((Long) map.get("articleId"));
        if (apArticleConfig != null) {
            apArticleConfig.setIsDown(isDown);
            apArticleConfigRepository.save(apArticleConfig);
        }

    }
}
