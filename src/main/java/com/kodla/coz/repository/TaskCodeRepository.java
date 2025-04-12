package com.kodla.coz.repository;

import com.kodla.coz.model.TaskCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCodeRepository extends JpaRepository<TaskCode, Integer> {
    @Query("SELECT tc FROM TaskCode tc WHERE tc.taskId=:taskId AND tc.languageId = :languageId")
    TaskCode findWhereTaskIdAndLanguageIdIs(int taskId, int languageId);

    List<TaskCode> findTaskCodesByTaskId(int id);

    TaskCode findByTaskIdAndLanguageId(Integer taskId, Integer languageId);
}
