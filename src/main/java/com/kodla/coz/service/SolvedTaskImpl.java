package com.kodla.coz.service;

import com.kodla.coz.model.SolvedTask;
import com.kodla.coz.repository.SolvedTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SolvedTaskImpl implements SolvedTaskService {

    @Autowired
    public SolvedTaskImpl(SolvedTaskRepository solvedTaskRepository) {
        this.solvedTaskRepository = solvedTaskRepository;
    }

    private final SolvedTaskRepository solvedTaskRepository;

    @Override
    public void save(SolvedTask solvedTask) {
        SolvedTask solvedTaskDb = solvedTaskRepository.findSolvedTaskByTaskIdAndUserId(solvedTask.getTaskId(), solvedTask.getUserId());
        if (solvedTaskDb != null){ // if user has solved this task before, update the code
            solvedTaskDb.setCode(solvedTask.getCode());
            solvedTaskRepository.save(solvedTaskDb);
        } else {
            solvedTaskRepository.save(solvedTask);
        }
    }

    @Override
    public SolvedTask findSolvedTaskByTaskIdAndUserId(Integer taskId, Integer userId) {
        return solvedTaskRepository.findSolvedTaskByTaskIdAndUserId(taskId, userId);
    }

    @Override
    public boolean isSolved(Integer taskId, Integer userId) {
        return solvedTaskRepository.findSolvedTaskByTaskIdAndUserId(taskId, userId) != null;
    }

    @Override
    public Integer countSolvedTasksByTaskId(Integer taskId) {
        return solvedTaskRepository.countSolvedTasksByTaskId(taskId);
    }

    @Override
    public Integer countTotalSolvedTasks() {
        return solvedTaskRepository.countTotalSolvedTasks();
    }
}
