package es.kiwi.wemedia.feign;

import es.kiwi.apis.wemedia.IWemediaClient;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.wemedia.pojos.WmUser;
import es.kiwi.wemedia.repository.WmChannelRepository;
import es.kiwi.wemedia.repository.WmUserRepository;
import es.kiwi.wemedia.service.WmChannelService;
import es.kiwi.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WemediaClient implements IWemediaClient {

    @Autowired
    private WmUserService wmUserService;

    @Autowired
    private WmChannelService wmChannelService;

    @Override
    @GetMapping("/api/v1/user/findByName/{name}")
    public Optional<WmUser> findWmUserByName(@PathVariable("name") String name) {
        return wmUserService.findByName(name);
    }

    @Override
    @PostMapping("/api/v1/wm_user/save")
    public ResponseResult saveWmUser(@RequestBody WmUser wmUser) {
        wmUserService.save(wmUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    @GetMapping("/api/v1/channel/list")
    public ResponseResult getChannels() {
        return wmChannelService.findAll();
    }

    @Override
    @GetMapping("/channels")
    public ResponseResult findAll() {
        return wmChannelService.findAll();
    }
}
