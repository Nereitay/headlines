package es.kiwi.model.wemedia.dtos;

import es.kiwi.model.common.dtos.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChannelDto extends PageRequestDto {
    /**
     * 频道名称
     */
    @ApiModelProperty(value = "频道名称")
    private String name;
}