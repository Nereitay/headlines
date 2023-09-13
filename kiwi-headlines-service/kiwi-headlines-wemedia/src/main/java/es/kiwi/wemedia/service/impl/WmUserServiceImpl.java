package es.kiwi.wemedia.service.impl;

import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.wemedia.dtos.WmLoginDto;
import es.kiwi.model.wemedia.pojos.WmUser;
import es.kiwi.utils.common.AppJwtUtil;
import es.kiwi.wemedia.repository.WmUserRepository;
import es.kiwi.wemedia.service.WmUserService;
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
@Transactional
@Slf4j
public class WmUserServiceImpl implements WmUserService {

    @Autowired
    private WmUserRepository wmUserRepository;

    @Override
    public ResponseResult login(WmLoginDto dto) {
        //1.检查参数
        if (StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "用户名或密码为空");
        }

        //2.查询用户
        Optional<WmUser> wmUserOptional = wmUserRepository.findByName(dto.getName());
        if (!wmUserOptional.isPresent()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        } else {
            WmUser wmUser = wmUserOptional.get();
            //3.比对密码
            String salt = wmUser.getSalt();
            String pswd = dto.getPassword();
            pswd = DigestUtils.md5DigestAsHex((pswd + salt).getBytes());
            if (pswd.equals(wmUser.getPassword())) {
                //4.返回数据  jwt
                Map<String,Object> map  = new HashMap<>();
                map.put("token", AppJwtUtil.getToken(wmUser.getId().longValue()));

                WmUser wmUserVO = new WmUser();
                BeanUtils.copyProperties(wmUser, wmUserVO);
                wmUserVO.setSalt("");
                wmUserVO.setPassword("");
                map.put("user", wmUserVO);

                return ResponseResult.okResult(map);
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

        }
    }
}
