package es.kiwi.wemedia.service.impl;

import es.kiwi.model.common.dtos.PageResponseResult;
import es.kiwi.model.common.dtos.ResponseResult;
import es.kiwi.model.common.enums.AppHttpCodeEnum;
import es.kiwi.model.wemedia.dtos.SensitiveDto;
import es.kiwi.model.wemedia.pojos.WmSensitive;
import es.kiwi.wemedia.repository.WmSensitiveRepository;
import es.kiwi.wemedia.service.WmSensitiveService;
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
public class WmSensitiveServiceImpl implements WmSensitiveService {

    @Autowired
    private WmSensitiveRepository wmSensitiveRepository;

    /**
     * 查询
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult list(SensitiveDto dto) {
        //1.检查参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //检查分页
        dto.checkParam();
        //2.模糊查询 + 分页
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        Page<WmSensitive> page = null;
        if (StringUtils.isNotBlank(dto.getName())) {
            page = wmSensitiveRepository.findBySensitivesContainingIgnoreCase(dto.getName(), pageable);
        } else {
            page = wmSensitiveRepository.findAll(pageable);
        }
        //3.结果返回
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotalElements());
        responseResult.setData(page.getContent());
        return responseResult;
    }

    /**
     * 新增
     *
     * @param wmSensitive
     * @return
     */
    @Override
    public ResponseResult insert(WmSensitive wmSensitive) {
        //1.检查参数
        if(wmSensitive == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //已存在的敏感词，不能保存
        WmSensitive wmSensitiveDB = wmSensitiveRepository.findBySensitives(wmSensitive.getSensitives());
        if (wmSensitiveDB != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "敏感词已存在");
        }

        //2.保存
        wmSensitive.setCreatedTime(new Date());
        wmSensitiveRepository.save(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 修改
     *
     * @param wmSensitive
     * @return
     */
    @Override
    public ResponseResult update(WmSensitive wmSensitive) {
        //1.检查参数
        if(wmSensitive == null || wmSensitive.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.修改
        Optional<WmSensitive> wmSensitiveOpt = wmSensitiveRepository.findById(wmSensitive.getId());
        wmSensitiveOpt.ifPresent(wmSensitiveDB -> {
            wmSensitiveDB.setSensitives(wmSensitive.getSensitives());
            wmSensitiveRepository.save(wmSensitiveDB);
        });
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
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.查询敏感词
        Optional<WmSensitive> wmSensitiveOpt = wmSensitiveRepository.findById(id);
        if (!wmSensitiveOpt.isPresent()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3.删除
        wmSensitiveRepository.deleteById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
