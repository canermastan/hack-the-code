package com.kodla.coz.service;

import com.kodla.coz.model.User;
import com.kodla.coz.repository.UserRepository;
import com.kodla.coz.utility.Utilities;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    public UserServiceImpl(@Lazy Utilities utilities, UserRepository userRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.utilities = utilities;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Utilities - UserService Circular dependency solved with @Lazy
    private final Utilities utilities;

    @Override
    public boolean save(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        User userCheck = null;
        userCheck = userRepository.findByEmail(user.getEmail());
        // if this email exists on database, return false
        if (userCheck != null) {
            return false;
        }
        // if this nickname exists on database, return false
        userCheck = userRepository.findByNickname(user.getNickname());
        if (userCheck != null) {
            return false;
        }

        // set hashed password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // create verification code and set user's verification code
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);

        user.setEnabled(true);

        // set user's score and level
        user.setTotalScore(10);
        user.setLevel(1);

        userRepository.save(user);

        // get created user from database and set USER role by id
        User userDb = userRepository.findByEmail(user.getEmail());
        userRepository.addUserRole(userDb.getId());

        // send verification email
        utilities.sendVerificationEmail(user, siteURL);
        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean changePassword(Principal principal, String oldPassword, String newPassword) {
        // get user from database by email
        User user = userRepository.findByEmail(principal.getName());
        // check if old password is correct
        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            // set new password
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public String findBiographyByEmail(String email) {
        return userRepository.findBiographyByEmail(email);
    }

    @Override
    public void updateBiographyByEmail(Principal principal, String biography) {
        userRepository.updateBiographyByEmail(principal.getName(), biography);
    }

    @Override
    public int findLevelByEmail(String email) {
        return userRepository.findLevelByEmail(email);
    }

    // update user level by USER ID
    @Override
    public void setLevelById(Integer userId) {
        userRepository.findById(userId).ifPresent(userDb -> {
            System.out.println(userDb.getTotalScore());
            if (userDb.getTotalScore() >= 100 && userDb.getTotalScore() <= 200) {
                updateLevel(userDb, 2);
            } else if (userDb.getTotalScore() >= 201 && userDb.getTotalScore() <= 300) {
                updateLevel(userDb, 3);
            } else if (userDb.getTotalScore() >= 301 && userDb.getTotalScore() <= 400) {
                updateLevel(userDb, 4);
            } else if (userDb.getTotalScore() >= 401 && userDb.getTotalScore() <= 500) {
                updateLevel(userDb, 5);
            } else if (userDb.getTotalScore() >= 501 && userDb.getTotalScore() <= 600) {
                updateLevel(userDb, 6);
            } else if (userDb.getTotalScore() >= 601 && userDb.getTotalScore() <= 700) {
                updateLevel(userDb, 7);
            } else if (userDb.getTotalScore() >= 701 && userDb.getTotalScore() <= 800) {
                updateLevel(userDb, 8);
            } else if (userDb.getTotalScore() >= 801 && userDb.getTotalScore() <= 900) {
                updateLevel(userDb, 9);
            } else if (userDb.getTotalScore() >= 901 && userDb.getTotalScore() <= 1000) {
                updateLevel(userDb, 10);
            } else if (userDb.getTotalScore() >= 1001 && userDb.getTotalScore() <= 1100) {
                updateLevel(userDb, 11);
            } else if (userDb.getTotalScore() >= 1101 && userDb.getTotalScore() <= 1200) {
                updateLevel(userDb, 12);
            } else if (userDb.getTotalScore() >= 1201 && userDb.getTotalScore() <= 1300) {
                updateLevel(userDb, 13);
            } else if (userDb.getTotalScore() >= 1301 && userDb.getTotalScore() <= 1400) {
                updateLevel(userDb, 14);
            } else if (userDb.getTotalScore() >= 1401 && userDb.getTotalScore() <= 1500) {
                updateLevel(userDb, 15);
            } else if (userDb.getTotalScore() >= 1501 && userDb.getTotalScore() <= 1600) {
                updateLevel(userDb, 16);
            } else if (userDb.getTotalScore() >= 1601 && userDb.getTotalScore() <= 1700) {
                updateLevel(userDb, 17);
            } else if (userDb.getTotalScore() >= 1701 && userDb.getTotalScore() <= 1800) {
                updateLevel(userDb, 18);
            } else if (userDb.getTotalScore() >= 1801 && userDb.getTotalScore() <= 1900) {
                updateLevel(userDb, 19);
            } else if (userDb.getTotalScore() >= 1901 && userDb.getTotalScore() <= 2000) {
                updateLevel(userDb, 20);
            } else if (userDb.getTotalScore() >= 2001 && userDb.getTotalScore() <= 2100) {
                updateLevel(userDb, 21);
            } else if (userDb.getTotalScore() >= 2101 && userDb.getTotalScore() <= 2200) {
                updateLevel(userDb, 22);
            } else if (userDb.getTotalScore() >= 2201 && userDb.getTotalScore() <= 2300) {
                updateLevel(userDb, 23);
            } else if (userDb.getTotalScore() >= 2301 && userDb.getTotalScore() <= 2400) {
                updateLevel(userDb, 24);
            } else if (userDb.getTotalScore() >= 2401 && userDb.getTotalScore() <= 2500) {
                updateLevel(userDb, 25);
            } else if (userDb.getTotalScore() >= 2501 && userDb.getTotalScore() <= 2600) {
                updateLevel(userDb, 26);
            } else if (userDb.getTotalScore() >= 2601 && userDb.getTotalScore() <= 2700) {
                updateLevel(userDb, 27);
            } else if (userDb.getTotalScore() >= 2701 && userDb.getTotalScore() <= 2800) {
                updateLevel(userDb, 28);
            } else if (userDb.getTotalScore() >= 2801 && userDb.getTotalScore() <= 2900) {
                updateLevel(userDb, 29);
            } else if (userDb.getTotalScore() >= 2901 && userDb.getTotalScore() <= 3000) {
                updateLevel(userDb, 30);
            } else if (userDb.getTotalScore() >= 3001 && userDb.getTotalScore() <= 3100) {
                updateLevel(userDb, 31);
            } else if (userDb.getTotalScore() >= 3101 && userDb.getTotalScore() <= 3200) {
                updateLevel(userDb, 32);
            } else if (userDb.getTotalScore() >= 3201 && userDb.getTotalScore() <= 3300) {
                updateLevel(userDb, 33);
            } else if (userDb.getTotalScore() >= 3301 && userDb.getTotalScore() <= 3400) {
                updateLevel(userDb, 34);
            } else if (userDb.getTotalScore() >= 3401 && userDb.getTotalScore() <= 3500) {
                updateLevel(userDb, 35);
            } else if (userDb.getTotalScore() >= 3501 && userDb.getTotalScore() <= 3600) {
                updateLevel(userDb, 36);
            } else if (userDb.getTotalScore() >= 3601 && userDb.getTotalScore() <= 3700) {
                updateLevel(userDb, 37);
            } else if (userDb.getTotalScore() >= 3701 && userDb.getTotalScore() <= 3800) {
                updateLevel(userDb, 38);
            } else if (userDb.getTotalScore() >= 3801 && userDb.getTotalScore() <= 3900) {
                updateLevel(userDb, 39);
            } else if (userDb.getTotalScore() >= 3901 && userDb.getTotalScore() <= 4000) {
                updateLevel(userDb, 40);
            }
        });
    }

    @Override
    public List<User> findLeaderboardUsers() {
        return userRepository.findLeaderboardUsers();
    }

    @Override
    public User setProfilePicture(Principal principal, String profilePicture) {
        // get user from database by email and set profile picture
        User userDb = userRepository.findByEmail(principal.getName());
        userDb.setProfilePicture(profilePicture);
        return userRepository.save(userDb);
    }

    @Override
    public int countBySolvedTasks(int userId) {
        return userRepository.countBySolvedTasks(userId);
    }

    @Override
    public List<User> findAllByVerifiedUsers() {
        return userRepository.findAllByVerifiedUsers();
    }

    private void updateLevel(User user, int level) {
        user.setLevel(level);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByNicknme(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void addUserRole(int id) {
        userRepository.addUserRole(id);
    }

    @Override
    public boolean verify(String verificationCode) {
        // get user from database by verification code
        User user = userRepository.findByVerificationCode(verificationCode);

        // if user is already enabled return false
        if (user == null) {
            return false;
        }

        // if verification code valid, delete verification code
        user.setVerificationCode(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public User findByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updateResetPasswordToken(String token, String email) {
        // get user from database by email
        User user = userRepository.findByEmail(email);
        if (user == null) { // if user not found
            throw new UsernameNotFoundException("Kullanıcı bulunamadı.");
        }
        // set reset password token
        user.setResetPasswordToken(token);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        // set hashed new password
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        // delete reset password token
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}
