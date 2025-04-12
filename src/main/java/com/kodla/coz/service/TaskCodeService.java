package com.kodla.coz.service;

import com.kodla.coz.model.TaskCode;

import java.util.List;
import java.util.Optional;

public interface TaskCodeService {
    TaskCode findWhereTaskIdAndLanguageIdIs(int taskId, int languageId);
    List<TaskCode> findTaskCodesByTaskId(int id);
    TaskCode findByTaskIdAndLanguageId(Integer taskId, Integer languageId);
    void save(TaskCode taskCode);
    void deleteById(Integer id);
    Optional<TaskCode> findById(int id);
}
