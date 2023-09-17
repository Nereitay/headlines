package es.kiwi.model.article.mapstruct.mappers;

import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.article.pojos.ApArticle;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface ApArticleMapper {

    ApArticleMapper INSTANCE = Mappers.getMapper(ApArticleMapper.class);

    ApArticle dtoToPojo(ArticleDto articleDto);
}
