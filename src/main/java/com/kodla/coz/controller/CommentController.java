package com.kodla.coz.controller;

import com.kodla.coz.model.Comment;
import com.kodla.coz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class CommentController {
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    private final CommentService commentService;

    /**
     * CommentList metodu göreve ait yorumları listeleyen sayfayı döndürür
     * @param model
     * @param taskId
     * @param pageNo
     * @return Thymeleaf Page
     */
    @GetMapping("/task/{id}/comments/{pageNo}")
    public String commentList(Model model, @PathVariable("id") Integer taskId, @PathVariable("pageNo") int pageNo) {
        Page<Comment> commentPage = commentService.getCommentsByTaskId(taskId, pageNo);

        // Comment object for new comment form
        model.addAttribute("comment", new Comment());

        // Comment list
        model.addAttribute("comments", commentPage.toList());

        // task id for comment form
        model.addAttribute("taskId", taskId);

        // for iteration pages
        model.addAttribute("totalPages", commentPage.getTotalPages());
        return "content/comments";
    }

    /**
     * AddComment metodu yeni yorum eklemek için kullanılan fonksiyondur
     * @param taskId
     * @param comment
     * @param principal
     * @return redirect
     */
    @PostMapping("/task/{id}/add-comment")
    public String addComment(@PathVariable("id") Integer taskId, @ModelAttribute("comment") Comment comment, Principal principal) {
        commentService.save(principal, comment, taskId);
        return "redirect:/task/" + taskId + "/comments/0";
    }

    /**
     * DeleteComment metodu kullanıcının kendi yazmış olduğu yorumu silmesi için kullanılan fonksiyondur
     * @param taskId
     * @param commentId
     * @return redirect
     */
    @PostMapping("/task/{taskId}/comment/{commentId}")
    public String deleteComment(
            @PathVariable("taskId") Integer taskId,
            @PathVariable("commentId") Integer commentId) {
        commentService.delete(commentId);
        return "redirect:/task/" + taskId + "/comments/0";
    }
}
