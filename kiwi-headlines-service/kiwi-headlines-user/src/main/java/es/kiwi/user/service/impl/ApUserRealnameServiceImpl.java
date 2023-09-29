package es.kiwi.user.service.impl;

import es.kiwi.apis.wemedia.IWemediaClient;
import es.kiwi.common.constants.UserConstants;
import es.kiwi.model.common.dtos.PageResponseResult;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.user.dtos.AuthDto;
import es.kiwi.model.user.pojos.ApUser;
import es.kiwi.model.user.pojos.ApUserRealname;
import es.kiwi.model.wemedia.pojos.WmUser;
import es.kiwi.user.repository.ApUserRealnameRepository;
import es.kiwi.user.repository.ApUserRepository;
import es.kiwi.user.service.ApUserRealnameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ApUserRealnameServiceImpl implements ApUserRealnameService {

    @Autowired
    private ApUserRealnameRepository apUserRealnameRepository;
    @Autowired
    private ApUserRepository apUserRepository;
    @Autowired
    private IWemediaClient wemediaClient;

    /**
     * 按照状态分页查询用户列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadListByStatus(AuthDto dto) {

        // 1.检查参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 分页条件检查
        dto.checkParam();

        // 2.分页根据状态精确查询
        Page<ApUserRealname> page = null;
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        if (dto.getStatus() != null) {
            page = apUserRealnameRepository.findByStatus(dto.getStatus(), pageable);
        } else {
            page = apUserRealnameRepository.findAll(pageable);
        }

        //3.结果返回
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotalElements());
        responseResult.setData(page.getContent());
        return responseResult;
    }

    /**
     * @param dto
     * @param status 2 审核失败    9 审核成功
     * @return
     */
    @Override
    public ResponseResult updateStatus(AuthDto dto, Short status) {
        //1.检查参数
        if (dto == null || dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.修改认证状态
        Optional<ApUserRealname> apUserRealnameOpt = apUserRealnameRepository.findById(dto.getId());
        apUserRealnameOpt.ifPresent(apUserRealname -> {
            apUserRealname.setStatus(status);
            if (StringUtils.isNotBlank(dto.getMsg())) {
                apUserRealname.setReason(dto.getMsg());
            }
            apUserRealnameRepository.save(apUserRealname);
        });

        //3.如果审核状态是9，就是成功，需要创建自媒体账户
        if (status.equals(UserConstants.PASS_AUTH)) {
            ResponseResult responseResult = createWmUserAndAuthor(dto);
            if (responseResult != null) {
                return responseResult;
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 创建自媒体账户
     * @param dto
     * @return
     */
    private ResponseResult createWmUserAndAuthor(AuthDto dto) {
        Integer userId = dto.getId();
        //查询用户认证信息
        Optional<ApUserRealname> apUserRealnameOpt = apUserRealnameRepository.findById(userId);
        if (!apUserRealnameOpt.isPresent()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        //查询app端用户信息
        ApUserRealname apUserRealname = apUserRealnameOpt.get();
        Optional<ApUser> apUserOpt = apUserRepository.findById(apUserRealname.getUserId());
        if (!apUserOpt.isPresent()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        //创建自媒体账户
        ApUser apUser = apUserOpt.get();
        Optional<WmUser> wmUserOpt = wemediaClient.findWmUserByName(apUser.getName());
        if (!wmUserOpt.isPresent()) {
            WmUser wmUser = new WmUser();
            wmUser.setApUserId(apUser.getId())
                    .setCreatedTime(new Date())
                    .setName(apUser.getName())
                    .setPassword(apUser.getPassword())
                    .setSalt(apUser.getSalt())
                    .setPhone(apUser.getPhone())
                    .setStatus(9);
            wemediaClient.saveWmUser(wmUser);
        }

        apUser.setFlag((short) 1);
        apUserRepository.save(apUser);

        return null;
    }
}
