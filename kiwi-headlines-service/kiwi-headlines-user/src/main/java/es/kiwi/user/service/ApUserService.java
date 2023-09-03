package es.kiwi.user.service;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.user.dtos.LoginDto;
import es.kiwi.model.user.pojos.ApUser;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ApUserService {

    ResponseResult login(LoginDto dto);


}
