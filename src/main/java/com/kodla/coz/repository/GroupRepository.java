package com.kodla.coz.repository;

import com.kodla.coz.model.Group;
import com.kodla.coz.model.Task;
import com.kodla.coz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g.tasks FROM Group g WHERE g.id = :groupId")
    List<Task> findTasksByGroupId(Integer groupId);

    @Query("SELECT g.users FROM Group g WHERE g.id = :groupId")
    List<User> findUsersByGroupId(Integer groupId);

    Optional<Group> findById(Integer groupId);

    Optional<Group> findByJoinCode(String joinCode);
}
