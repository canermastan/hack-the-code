package com.kodla.coz.repository;

import com.kodla.coz.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t")
    List<Task> findAll();

    // Find all tasks by LANGUAGE ID with order by SCORE ASC
    @Query("SELECT t FROM Task t WHERE t.language.id = :id ORDER BY t.score ASC")
    List<Task> findWhereLanguageIdIsOrderByScoreAsc(Integer id);

    // Find all tasks by LANGUAGE ID and DIFFICULTY
    @Query(value = "SELECT * FROM tasks t WHERE t.language_id = :id AND t.difficulty IN :difficulties", nativeQuery = true)
    List<Task> findWhereLanguageIdIsAndDifficultyIs(Integer id, @Param("difficulties") List<String> difficulties);

    // Find all tasks by LANGUAGE ID, DIFFICULTY and SOLVED
    @Query("SELECT t FROM Task t JOIN SolvedTask s ON t.id = s.taskId WHERE s.userId = :userId AND t.language.id = :languageId AND t.difficulty IN :difficulties")
    List<Task> findWhereLanguageIdIsAndDifficultyIsAndSolved(Integer userId, Integer languageId, @Param("difficulties") List<String> difficulties);

    // Find all tasks by LANGUAGE ID, DIFFICULTY and UNSOLVED
    @Query("SELECT t FROM Task t JOIN SolvedTask s ON t.id = s.taskId WHERE s.userId <> :userId AND t.difficulty IN :difficulties AND t.language.id = :languageId") // s.userId != :userId
    List<Task> findWhereLanguageIdIsAndDifficultyIsAndUnsolved(Integer userId, Integer languageId, @Param("difficulties") List<String> difficulties);

    // Find all tasks by LANGUAGE ID and SOLVED
    @Query("SELECT t FROM Task t JOIN SolvedTask s ON t.id = s.taskId WHERE s.userId = :userId AND t.language.id = :languageId")
    List<Task> findWhereLanguageIdIsAndSolved(Integer userId, Integer languageId);

    // Find all tasks by LANGUAGE ID and UNSOLVED
    @Query("SELECT t FROM Task t JOIN SolvedTask s ON t.id = s.taskId WHERE t.language.id = :languageId AND s.userId <> :userId") // s.userId != :userId
    List<Task> findWhereLanguageIdIsAndUnsolved(Integer userId, Integer languageId);

    // Find task language for task title
    @Query(value = "SELECT name FROM languages l INNER JOIN tasks t ON t.language_id = l.id WHERE t.id = :taskId",
    nativeQuery = true)
    String findTaskLanguage(Integer taskId);

    /**
     * When the task is solved, add score to user
     * @param score
     * @param userId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET total_score = total_score + :score WHERE users.user_id = :userId", nativeQuery = true)
    void addScoreToUser(int score, Integer userId);
}
