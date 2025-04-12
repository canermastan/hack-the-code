package com.kodla.coz.model;

import com.kodla.coz.model.audit.AuditDateWithoutUpdatedDate;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends AuditDateWithoutUpdatedDate<String> {

    public Comment() {

    }

    public Comment(Integer id, String nickname, Integer taskId, String body, Integer userId) {
        this.id = id;
        this.nickname = nickname;
        this.taskId = taskId;
        this.body = body;
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String nickname;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(length = 2000)
    private String body;

    @Column(name = "user_id")
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
