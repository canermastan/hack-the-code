package com.kodla.coz.config.security;

import com.kodla.coz.model.User;
import com.kodla.coz.model.UserLoginHistory;
import com.kodla.coz.repository.UserLoginHistoryRepository;
import com.kodla.coz.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    private final UserService userService;
    private final UserLoginHistoryRepository userLoginHistoryRepository;

    public AuthenticationSuccessHandler(UserService userService, UserLoginHistoryRepository userLoginHistoryRepository) {
        this.userService = userService;
        this.userLoginHistoryRepository = userLoginHistoryRepository;
    }

    /**
     * OnAuthenticationSuccess metodu kullanıcının son giriş tarihini kaydeden fonksiyondur
     * Giriş yapıldıktan sonra bu fonksiyon çalışır ve kullanıcı dashboard'a yönlendirilir
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // Set last login date
        User user = userService.findByEmail(authentication.getName());

        userLoginHistoryRepository.findByUserId(user.getId()).ifPresentOrElse(
                u -> {
                    u.setLoginDate(new Date());
                    userLoginHistoryRepository.save(u);
                },
                () -> {
                    UserLoginHistory userLoginHistory = new UserLoginHistory();
                    userLoginHistory.setUser(user);
                    userLoginHistory.setLoginDate(new Date());
                    userLoginHistoryRepository.save(userLoginHistory);
                });

        response.sendRedirect("/dashboard");
    }
}
