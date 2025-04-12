package com.kodla.coz.service;

import com.kodla.coz.model.Comment;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface CommentService {
    Comment save(Principal principal, Comment comment, int taskId);
    void delete(Integer id);

    /**
     * Göreve ait yorumları döndürür
     * @param taskId
     * @param pageNo
     * @return 10 comments per page
     */
    Page<Comment> getCommentsByTaskId(Integer taskId, int pageNo);
}
