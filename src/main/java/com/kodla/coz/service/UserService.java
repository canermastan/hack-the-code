package com.kodla.coz.service;

import com.kodla.coz.model.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean save(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void save(User user);
    User findByEmail(String email);
    User findByNicknme(String nickname);
    Optional<User> findById(Integer id);
    List<User> findAll();
    void addUserRole(int id);
    boolean verify(String verificationCode);
    User findByResetPasswordToken(String token);
    void updateResetPasswordToken(String token, String email);
    void updatePassword(User user, String newPassword);
    boolean changePassword(Principal principal, String oldPassword, String newPassword);
    String findBiographyByEmail(String email);
    void updateBiographyByEmail(Principal principal, String biography);
    int findLevelByEmail(String email);
    void setLevelById(Integer userId);
    List<User> findLeaderboardUsers();
    User setProfilePicture(Principal principal, String profilePicture);
    int countBySolvedTasks(int userId);
    List<User> findAllByVerifiedUsers();
}
