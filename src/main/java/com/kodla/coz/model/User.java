package com.kodla.coz.model;

import com.kodla.coz.model.audit.AuditDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AuditDate<String> implements Serializable {
    public User(){

    }

    public User(Integer id, String verificationCode, String email, String password, int totalScore, int level, String fullName, boolean enabled, String nickname, String profilePicture, String biography, Set<Role> roles, String resetPasswordToken, List<UserLoginHistory> loginHistory) {
        this.id = id;
        this.verificationCode = verificationCode;
        this.email = email;
        this.password = password;
        this.totalScore = totalScore;
        this.level = level;
        this.fullName = fullName;
        this.enabled = enabled;
        this.nickname = nickname;
        this.profilePicture = profilePicture;
        this.biography = biography;
        this.roles = roles;
        this.resetPasswordToken = resetPasswordToken;
        this.loginHistory = loginHistory;
    }

    @Transient
    public String getProfilePictureImagePath() {
        if (profilePicture == null || id == null) return "/user-photos/" + "default_user_KODLACOZ.png";

        return "/user-photos/" + id + "/" + profilePicture;
    }

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "total_score")
    private int totalScore;

    @Column
    private int level;

    @Column(length = 30)
    private String fullName;

    @Column(length = 20)
    private boolean enabled;

    @Column
    private String nickname;

    @Column(name = "profile_picture", length = 64)
    private String profilePicture;

    @Column(length = 500)
    private String biography;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @OneToMany(mappedBy = "user")
    private List<UserLoginHistory> loginHistory;

    @ManyToMany(mappedBy = "users")
    private List<Group> groups;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public List<UserLoginHistory> getLoginHistory() {
        return loginHistory;
    }

    public void setLoginHistory(List<UserLoginHistory> loginHistory) {
        this.loginHistory = loginHistory;
    }

    /** başlangıçta totalScore ile bir sonraki levela geçmek için gereken totalScore arasındaki fark her zaman 100dür
           totalScore'un son iki hanesini yüzde olarak alabiliriz
           Bu fonksiyon progressbarın olduğu thymeleaf sayfalarında yüzdeyi göstermek için kullanılır
         */
    public int getLevelPercentage() {
        int birler = (totalScore % 100) % 10;
        int onlar = (totalScore % 100) / 10;
        String number = onlar + "" + birler;
        return Integer.parseInt(number);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
