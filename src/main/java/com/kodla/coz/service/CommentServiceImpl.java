package com.kodla.coz.service;

import com.kodla.coz.model.User;
import com.kodla.coz.repository.CommentRepository;
import com.kodla.coz.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService){
        this.commentRepository = commentRepository;
        this.userService = userService;
    }
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public Comment save(Principal principal, Comment comment, int taskId) {
        User userDb = userService.findByEmail(principal.getName());

        comment.setTaskId(taskId);
        comment.setUserId(userDb.getId());
        comment.setNickname(userDb.getNickname());

        return commentRepository.save(comment);
    }

    @Override
    public void delete(Integer id) {
        commentRepository.findById(id).ifPresent(commentRepository::delete);
    }

    @Override
    public Page<Comment> getCommentsByTaskId(Integer taskId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("id"));
        return commentRepository.getCommentsByTaskId(taskId, pageable);
    }
}
