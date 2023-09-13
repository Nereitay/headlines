package es.kiwi.wemedia.service.impl;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.wemedia.repository.WmChannelRepository;
import es.kiwi.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl implements WmChannelService {

    @Autowired
    private WmChannelRepository wmChannelRepository;

    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(wmChannelRepository.findAll());
    }
}
