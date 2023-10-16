package es.kiwi.behavior.controller.v1;

import es.kiwi.behavior.service.ApUnlikesBehaviorService;
import es.kiwi.model.behavior.dtos.UnlikesBehaviorDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/un_likes_behavior")
public class ApUnlikesBehaviorController {

    @Autowired
    private ApUnlikesBehaviorService apUnlikesBehaviorService;

    @PostMapping
    public ResponseResult unlike(@RequestBody UnlikesBehaviorDto dto) {
        return apUnlikesBehaviorService.unlike(dto);
    }
}