package es.kiwi.comment.service;

import es.kiwi.model.comment.dtos.CommentSaveDto;
import es.kiwi.model.common.dtos.ResponseResult;

public interface CommentService {
    /**
     * 保存评论
     * @param dto
     * @return
     */
    ResponseResult saveComment(CommentSaveDto dto);
}
