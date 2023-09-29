package es.kiwi.admin.service.impl;

import es.kiwi.admin.repository.AdUserRepository;
import es.kiwi.admin.service.AdUserService;
import es.kiwi.model.admin.dtos.AdUserDto;
import es.kiwi.model.admin.pojos.AdUser;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AdUserServiceImpl implements AdUserService {

    @Autowired
    private AdUserRepository adUserRepository;

    /**
     * 登录
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(AdUserDto dto) {
        //1.检查参数
        String name = dto.getName();
        String password = dto.getPassword();
        if (StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名或密码为空");
        }

        //2.查询用户
        Optional<AdUser> adUserOpt = adUserRepository.findByName(name);
        if (!adUserOpt.isPresent()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        } else { //3.比对密码
            AdUser adUser = adUserOpt.get();
            String salt = adUser.getSalt();
            password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if (password.equals(adUser.getPassword())) {
                //4.返回数据  jwt
                Map<String, Object> map = new HashMap<>();
                map.put("token", AppJwtUtil.getToken(adUser.getId().longValue()));
                AdUser adUserVo = new AdUser();
                BeanUtils.copyProperties(adUser, adUserVo);
                adUserVo.setSalt("");
                adUserVo.setPassword("");
                map.put("user", adUserVo);
                return ResponseResult.okResult(map);
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
        }
    }
}
