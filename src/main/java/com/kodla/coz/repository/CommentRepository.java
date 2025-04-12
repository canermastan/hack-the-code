package com.kodla.coz.repository;

import com.kodla.coz.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // Find all comments by TASK ID
    @Query("SELECT c FROM Comment c JOIN User u ON u.id = c.userId WHERE c.taskId = :taskId")
    Page<Comment> getCommentsByTaskId(Integer taskId, Pageable pageable);
}
