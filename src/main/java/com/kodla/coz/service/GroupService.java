package com.kodla.coz.service;

import com.kodla.coz.model.Group;
import com.kodla.coz.model.Task;
import com.kodla.coz.model.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group save(Group group);
    void update(Group group);
    void delete(Group group);
    List<Task> findTasksByGroupId(Integer groupId);
    List<User> findUsersByGroupId(Integer groupId);
    Optional<Group> findById(Integer groupId);

    double calculateTaskCompletionPercentage(Group group);
    long getCompletedTaskCount(Group group);
    int getTotalTaskCount(Group group);

    boolean joinGroup(String joinCode, User user);
    Optional<Group> findByJoinCode(String joinCode);

    boolean isUserInGroup(Integer groupId, User user);
}
