package com.kodla.coz.model;

import org.springframework.stereotype.Component;

@Component
public class Compiler {
    public Compiler(){

    }

    public Compiler(String language, String code, int taskId, boolean isForTest) {
        this.language = language;
        this.code = code;
        this.taskId = taskId;
        this.isForTest = isForTest;
    }

    private String language;
    private String code;

    private int taskId;
    private boolean isForTest;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean getIsForTest() {
        return isForTest;
    }

    public void setForTest(boolean forTest) {
        isForTest = forTest;
    }
}
