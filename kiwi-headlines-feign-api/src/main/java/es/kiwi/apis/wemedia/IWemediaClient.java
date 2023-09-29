package es.kiwi.apis.wemedia;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.wemedia.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient("headlines-wemedia")
public interface IWemediaClient {

    @GetMapping("/api/v1/user/findByName/{name}")
    Optional<WmUser> findWmUserByName(@PathVariable("name") String name);

    @PostMapping("/api/v1/wm_user/save")
    ResponseResult saveWmUser(@RequestBody WmUser wmUser);

    @GetMapping("/api/v1/channel/list")
    ResponseResult getChannels();
}
