package com.kodla.coz.model;

import javax.persistence.*;

@Entity
@Table(name = "task_codes")
public class TaskCode {
    public TaskCode(){}
    public TaskCode(int id, String code, String testCode, int languageId, int taskId) {
        this.id = id;
        this.code = code;
        this.testCode = testCode;
        this.languageId = languageId;
        this.taskId = taskId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 4000)
    private String code;
    @Column(name = "test_code", length = 4000)
    private String testCode;
    @Column(name = "language_id")
    private int languageId;
    @Column(name = "task_id")
    private int taskId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
