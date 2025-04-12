package com.kodla.coz.service;

import com.kodla.coz.model.Group;
import com.kodla.coz.model.Task;
import com.kodla.coz.model.User;
import com.kodla.coz.repository.GroupRepository;
import com.kodla.coz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class GroupServiceImpl implements GroupService{

    private final GroupRepository groupRepository;
    private final UserService userService;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }


    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void update(Group group) {
        groupRepository.save(group);
    }

    @Override
    public void delete(Group group) {
        groupRepository.delete(group);
    }

    @Override
    public List<Task> findTasksByGroupId(Integer groupId) {
        return groupRepository.findTasksByGroupId(groupId);
    }

    @Override
    public List<User> findUsersByGroupId(Integer groupId) {
        return groupRepository.findUsersByGroupId(groupId);
    }

    @Override
    public Optional<Group> findById(Integer groupId) {
        return groupRepository.findById(groupId);
    }

    @Override
    public double calculateTaskCompletionPercentage(Group group) {
        return group.getTaskCompletionPercentage();
    }

    @Override
    public long getCompletedTaskCount(Group group) {
        return group.getCompletedTaskCount();
    }

    @Override
    public int getTotalTaskCount(Group group) {
        return group.getTotalTaskCount();
    }

    @Override
    public boolean joinGroup(String joinCode, User user) {
        Optional<Group> groupOptional = groupRepository.findByJoinCode(joinCode);

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();

            if (!group.getUsers().contains(user)) {
                group.getUsers().add(user);
            } else {
                return false;
            }

            if (!user.getGroups().contains(group)) {
                user.getGroups().add(group);
            } else {
                return false;
            }

            groupRepository.save(group);
            userService.save(user);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Group> findByJoinCode(String joinCode) {
        return groupRepository.findByJoinCode(joinCode);
    }

    @Override
    public boolean isUserInGroup(Integer groupId, User user) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            return group.getUsers().stream().anyMatch(u -> u.getId().equals(user.getId()));
        }
        return false;
    }
}
