package com.kodla.coz.model;

import com.kodla.coz.model.audit.AuditDate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group extends AuditDate<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Column(name = "admin_id")
    private Integer adminId;
    @Column
    @ManyToMany
    @JoinTable(
            name = "group_users",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;
    @ManyToMany
    @JoinTable(
            name = "group_tasks",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

    @Column(name = "join_code")
    private String joinCode;

    public Group(){

    }
    public Group(Integer id, String name, String description, Integer adminId, List<User> users, List<Task> tasks, String joinCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.adminId = adminId;
        this.users = users;
        this.tasks = tasks;
        this.joinCode = joinCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    // Görev tamamlama oranını hesaplayan yöntem
    public double getTaskCompletionPercentage() {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }

        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        return (double) completedTasks / tasks.size() * 100;
    }

    // Tamamlanan görev sayısını döndüren yöntem
    public long getCompletedTaskCount() {
        return tasks.stream().filter(Task::isCompleted).count();
    }

    // Toplam görev sayısını döndüren yöntem
    public int getTotalTaskCount() {
        return tasks.size();
    }
}
