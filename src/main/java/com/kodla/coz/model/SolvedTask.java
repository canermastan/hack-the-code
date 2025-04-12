package com.kodla.coz.model;

import com.kodla.coz.model.audit.AuditDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "solved_tasks")
public class SolvedTask extends AuditDate<String> implements Serializable {

    public SolvedTask() {

    }

    public SolvedTask(Integer id, Integer userId, Integer taskId, String code) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.code = code;
    }

    public SolvedTask(String createdBy, Date createdDate, String updatedBy, Date updatedDate, Integer id, Integer userId, Integer taskId, String code) {
        super(createdBy, createdDate, updatedBy, updatedDate);
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "task_id")
    private Integer taskId;
    @Column(length = 4000)
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
