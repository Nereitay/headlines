package es.kiwi.admin.controller.v1;

import es.kiwi.admin.service.AdUserService;
import es.kiwi.model.admin.dtos.AdUserDto;
import es.kiwi.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AdUserService adUserService;

    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdUserDto dto) {
        return adUserService.login(dto);
    }
}
