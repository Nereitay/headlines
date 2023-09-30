package es.kiwi.model.wemedia.vos;

import es.kiwi.model.wemedia.pojos.WmNews;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WmNewsVo extends WmNews {
    /**
     * 作者名称
     */
    private String authorName;
}