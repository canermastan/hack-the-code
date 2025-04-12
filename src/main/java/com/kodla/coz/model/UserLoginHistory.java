package com.kodla.coz.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_login_histories")
public class UserLoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "login_date")
    private Date loginDate;

    public UserLoginHistory() {
    }

    public UserLoginHistory(Integer id, User user, Date loginDate) {
        this.id = id;
        this.user = user;
        this.loginDate = loginDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}
