package com.kodla.coz.repository;

import com.kodla.coz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Find User by E-MAIL
    User findByEmail(String email);

    // Find User by USERNAME/NICKNAME
    User findByNickname(String nickname);

    // Add role to user when user is registered
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_roles VALUES (:user_id, 1)", // 1 -> user | 2 -> admin
            nativeQuery = true)
    void addUserRole(@Param("user_id") int id);

    // Find User by VERIFICATION CODE for email verification
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1") // ?1 is the first parameter (String code)
    User findByVerificationCode(String code);

    //Find User by RESET PASSWORD TOKEN for password reset
    @Query("SELECT u FROM User u WHERE u.resetPasswordToken = :token")
    User findByResetPasswordToken(String token);

    // find User's Biography by E-MAIL
    @Query("SELECT u.biography FROM User u WHERE u.email = :email")
    String findBiographyByEmail(String email);

    // update biography by E-MAIL
    @Modifying
    @Query("UPDATE User u SET u.biography = :biography WHERE u.email = :email")
    void updateBiographyByEmail(String email, String biography);

    // find user level by E-MAIL
    @Query("SELECT u.level FROM User u WHERE u.email = :email")
    int findLevelByEmail(String email);

    // find top 5 leaderboard users ORDER BY totalScore DESC
    @Query(value = "SELECT * from users u ORDER BY u.total_score DESC LIMIT 5",
    nativeQuery = true)
    List<User> findLeaderboardUsers();

    @Query(value = "SELECT COUNT(*) FROM solved_tasks WHERE user_id = :userId", nativeQuery = true)
    int countBySolvedTasks(int userId);

    @Query(value = "SELECT u FROM User u WHERE u.verificationCode IS NULL")
    List<User> findAllByVerifiedUsers();
}
