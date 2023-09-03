package es.kiwi.user.service.impl;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.user.dtos.LoginDto;
import es.kiwi.model.user.pojos.ApUser;
import es.kiwi.user.repository.ApUserRepository;
import es.kiwi.user.service.ApUserService;
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

@Service("apUserService")
@Transactional
@Slf4j
public class ApUserServiceImpl implements ApUserService {

    @Autowired
    private ApUserRepository apUserRepository;

    /**
     * app端登录功能
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
        String phone = dto.getPhone();
        String password = dto.getPassword();

        // 1. 正常登录 用户名和密码
        if(StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(password)) {
            // 1.1 根据手机号查询用户信息
            Optional<ApUser> apUserOpt = apUserRepository.findByPhone(phone);
            if (!apUserOpt.isPresent()) {
                return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
            }

            // 1.2 比对密码
            ApUser dbUser = apUserOpt.get();
            String salt = dbUser.getSalt();
            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes()); // 加密
            if (!pswd.equals(dbUser.getPassword())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            // 1.3 返回数据 jwt user
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            ApUser apUser = new ApUser();

            BeanUtils.copyProperties(dbUser, apUser);
            apUser.setSalt("");
            apUser.setPassword("");
            map.put("user", apUser);

            return ResponseResult.okResult(map);
        } else {
            // 2. 游客登录
            Map<String, Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }

    }

}
