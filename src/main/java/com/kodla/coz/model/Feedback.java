package com.kodla.coz.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kodla.coz.model.audit.AuditDateWithoutUpdatedDate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    public Feedback(Integer id, String email, String feedbackType, String message, String url, LocalDateTime createdDate) {
        this.id = id;
        this.email = email;
        this.feedbackType = feedbackType;
        this.message = message;
        this.url = url;
        this.createdDate = createdDate;
    }

    public Feedback() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @Column
    private String email;
    @JsonProperty("feedbackType")
    @Column(name = "feedback_type")
    private String feedbackType;
    @Column
    private String message;
    @Column
    private String url;
    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
