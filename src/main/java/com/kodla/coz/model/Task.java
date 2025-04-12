package com.kodla.coz.model;

import com.kodla.coz.model.audit.AuditDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task extends AuditDate<String> implements Serializable {
    public Task() {

    }

    public Task(Integer id, String title, String difficulty, String content, float successRate, int score, float rating, Language language, Boolean completed) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.content = content;
        this.successRate = successRate;
        this.score = score;
        this.rating = rating;
        this.language = language;
        this.completed = completed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String difficulty;

    @Column(length = 10000)
    private String content;

    @Column(name = "success_rate")
    private float successRate;

    @Column
    private int score;

    @Column
    private float rating;

    @ManyToOne(cascade = CascadeType.ALL)
    private Language language;

    @OneToMany
    @JoinColumn(name = "task_id")
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "task_id")
    private final List<TaskCode> taskCodes = new ArrayList<>();

    @ManyToMany(mappedBy = "tasks")
    private List<Group> groups;

    @Column
    private Boolean completed;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Comment> getAnswers() {
        return comments;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
