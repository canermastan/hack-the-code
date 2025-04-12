package com.kodla.coz.service;

import com.kodla.coz.model.SolvedTask;

public interface SolvedTaskService {
    void save(SolvedTask solvedTask);
    SolvedTask findSolvedTaskByTaskIdAndUserId(Integer taskId, Integer userId);
    boolean isSolved(Integer taskId, Integer userId);
    Integer countSolvedTasksByTaskId(Integer taskId);
    Integer countTotalSolvedTasks();
}
