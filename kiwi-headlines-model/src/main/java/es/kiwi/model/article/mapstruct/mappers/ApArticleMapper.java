package es.kiwi.model.article.mapstruct.mappers;

import es.kiwi.model.article.dtos.ArticleDto;
import es.kiwi.model.article.pojos.ApArticle;
import es.kiwi.model.article.vos.HotArticleVo;
import es.kiwi.model.search.vos.SearchArticleVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface ApArticleMapper {

    ApArticleMapper INSTANCE = Mappers.getMapper(ApArticleMapper.class);

    ApArticle dtoToPojo(ArticleDto articleDto);

    SearchArticleVo apArticleToSearchArticleVo(ApArticle apArticle);

    HotArticleVo pojoToHotArticleVo (ApArticle apArticle);
}
