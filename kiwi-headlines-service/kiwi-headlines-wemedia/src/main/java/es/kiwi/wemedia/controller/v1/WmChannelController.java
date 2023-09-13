package es.kiwi.wemedia.controller.v1;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/channel")
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;
    @GetMapping("/channels")
    public ResponseResult findAll() {
        return wmChannelService.findAll();
    }
}
