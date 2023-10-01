package es.kiwi.behavior.controller.v1;

import es.kiwi.behavior.service.ApLikesBehaviorService;
import es.kiwi.model.behavior.dtos.LikesBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/likes_behavior")
public class ApLikesBehaviorController {

    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;

    @PostMapping
    public ResponseResult like(@RequestBody LikesBehaviorDto dto) {
        return apLikesBehaviorService.like(dto);
    }
}
