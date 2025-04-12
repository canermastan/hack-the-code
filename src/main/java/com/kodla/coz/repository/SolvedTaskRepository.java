package com.kodla.coz.repository;

import com.kodla.coz.model.SolvedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedTaskRepository extends JpaRepository<SolvedTask, Integer> {
    // Find Solved Task by TASK ID and USER ID
    SolvedTask findSolvedTaskByTaskIdAndUserId(Integer taskId, Integer userId);
    // For task's success rate
    @Query("SELECT COUNT(s) FROM SolvedTask s WHERE s.taskId = :taskId")
    Integer countSolvedTasksByTaskId(Integer taskId);
    @Query("SELECT COUNT(s) FROM SolvedTask s")
    Integer countTotalSolvedTasks();
}
