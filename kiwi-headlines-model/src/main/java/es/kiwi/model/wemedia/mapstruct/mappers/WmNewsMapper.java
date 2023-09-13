package es.kiwi.model.wemedia.mapstruct.mappers;

import es.kiwi.model.wemedia.dtos.WmNewsDto;
import es.kiwi.model.wemedia.pojos.WmNews;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WmNewsMapper {
    WmNewsMapper INSTANCE = Mappers.getMapper(WmNewsMapper.class);


    @Mappings({
            @Mapping(
                    target = "images",
                    expression = "java(wmNewsDto.getImages() != null && wmNewsDto.getImages().size() > 0 ? String.join(\",\", wmNewsDto.getImages()) : null)"),
            @Mapping(target = "type", expression = "java(wmNewsDto.getType().equals((short)-1) ? null : wmNewsDto.getType())")
    })
    WmNews dtoToPojo(WmNewsDto wmNewsDto);
}
