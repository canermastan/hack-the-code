package com.kodla.coz.service;

import com.kodla.coz.model.Task;
import com.kodla.coz.model.Compiler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();
    Task save(Task task);
    void deleteById(Integer id);
    Task update(Task task);
    List<Task> findWhereLanguageIdIs(Integer id);
    Optional<Task> findById(Integer id);
    String findTaskLanguage(Integer taskId);
    boolean isPassedTest(String language, String output);
    void addScoreToUser(int score, Integer userId);
    List<Task> findWhereLanguageIdIsAndDifficultyIs(Integer id, List<String> difficulties);
    List<Task> findWhereLanguageIdIsAndDifficultyIsAndSolved(Integer userId, Integer languageId, List<String> difficulties);
    List<Task> findWhereLanguageIdIsAndDifficultyIsAndUnsolved(Integer userId, Integer languageId, List<String> difficulties);
    List<Task> findWhereLanguageIdIsAndUnsolved(Integer userId, Integer languageId);
    List<Task> findWhereLanguageIdIsAndSolved(Integer userId, Integer languageId);
    String compile(Compiler compiler, int userId) throws IOException;
}
