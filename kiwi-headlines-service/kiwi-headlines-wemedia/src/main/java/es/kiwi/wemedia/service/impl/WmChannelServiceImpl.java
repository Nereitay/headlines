package es.kiwi.wemedia.service.impl;

import es.kiwi.model.common.dtos.PageResponseResult;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.wemedia.dtos.ChannelDto;
import es.kiwi.model.wemedia.pojos.WmChannel;
import es.kiwi.wemedia.repository.WmChannelRepository;
import es.kiwi.wemedia.repository.WmNewsRepository;
import es.kiwi.wemedia.service.WmChannelService;
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
@Transactional
@Slf4j
public class WmChannelServiceImpl implements WmChannelService {

    @Autowired
    private WmChannelRepository wmChannelRepository;

    @Autowired
    private WmNewsRepository wmNewsRepository;

    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(wmChannelRepository.findAll());
    }

    /**
     * 查询
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findByNameAndPage(ChannelDto dto) {
        //1.检查参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检查分页
        dto.checkParam();

        //2.模糊查询+分页
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        Page<WmChannel> page = null;
        if (StringUtils.isNotBlank(dto.getName())) {//频道名称模糊查询
            page = wmChannelRepository.findByNameContainsIgnoreCase(dto.getName(), pageable);
        } else {
            page = wmChannelRepository.findAll(pageable);
        }

        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotalElements());
        responseResult.setData(page.getContent());
        return responseResult;
    }

    /**
     * 新增
     *
     * @param wmChannel
     * @return
     */
    @Override
    public ResponseResult insert(WmChannel wmChannel) {
        //1.检查参数
        if (wmChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmChannel wmChannelDB = wmChannelRepository.findByName(wmChannel.getName());
        if (wmChannelDB != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "频道已存在");
        }

        //2.保存
        wmChannel.setCreatedTime(new Date());
        wmChannelRepository.save(wmChannel);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 修改
     *
     * @param wmChannel
     * @return
     */
    @Override
    public ResponseResult update(WmChannel wmChannel) {
        //1.检查参数
        if (wmChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //判断是否被引用
        Integer count = wmNewsRepository.countAllByChannelId(wmChannel.getId());
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道被引用不能修改或禁用");
        }

        //2.修改
        Optional<WmChannel> wmChannelOpt = wmChannelRepository.findById(wmChannel.getId());
        WmChannel wmChannelDB = wmChannelOpt.get();
        wmChannelDB.setName(wmChannel.getName());
        wmChannelDB.setDescription(wmChannel.getDescription());
        wmChannelDB.setStatus(wmChannel.getStatus());
        wmChannelDB.setOrd(wmChannel.getOrd());
        wmChannelRepository.save(wmChannelDB);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delete(Integer id) {
        //1.检查参数
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.查询频道
        Optional<WmChannel> wmChannelOpt = wmChannelRepository.findById(id);
        if (!wmChannelOpt.isPresent()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3.频道是否有效
        WmChannel wmChannel = wmChannelOpt.get();
        if (wmChannel.getStatus()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道有效，不能删除");
        }
        //判断是否被引用
        Integer count = wmNewsRepository.countAllByChannelId(wmChannel.getId());
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道被引用不能删除");
        }
        //4.删除
        wmChannelRepository.deleteById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
