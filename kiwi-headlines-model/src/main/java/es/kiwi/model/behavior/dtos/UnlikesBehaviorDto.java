package es.kiwi.model.behavior.dtos;

import es.kiwi.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UnlikesBehaviorDto {
    // 文章ID
    @IdEncrypt
    Long articleId;

    /**
     * 不喜欢操作方式
     * 0 不喜欢
     * 1 取消不喜欢
     */
    Short type;
}
