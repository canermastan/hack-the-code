package com.kodla.coz.service;

import com.kodla.coz.repository.TaskCodeRepository;
import com.kodla.coz.model.TaskCode;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskCodeServiceImpl implements TaskCodeService {

    private final TaskCodeRepository taskCodeRepository;

    public TaskCodeServiceImpl(TaskCodeRepository taskCodeRepository){
        this.taskCodeRepository = taskCodeRepository;
    }

    @Override
    public TaskCode findWhereTaskIdAndLanguageIdIs(int taskId, int languageId) {
        return taskCodeRepository.findWhereTaskIdAndLanguageIdIs(taskId, languageId);
    }

    @Override
    public List<TaskCode> findTaskCodesByTaskId(int id) {
        return taskCodeRepository.findTaskCodesByTaskId(id);
    }

    @Override
    public TaskCode findByTaskIdAndLanguageId(Integer taskId, Integer languageId) {
        return taskCodeRepository.findByTaskIdAndLanguageId(taskId, languageId);
    }

    @Override
    public void save(TaskCode taskCode) {
        taskCodeRepository.save(taskCode);
    }

    @Override
    public void deleteById(Integer id) {
        taskCodeRepository.deleteById(id);
    }

    @Override
    public Optional<TaskCode> findById(int id) {
        return taskCodeRepository.findById(id);
    }
}
