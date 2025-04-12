package com.kodla.coz.controller;

import com.kodla.coz.model.User;
import com.kodla.coz.service.UserService;
import com.kodla.coz.utility.Utilities;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.regex.Pattern;

@Controller
public class AuthController {
    @Autowired
    public AuthController(UserService userService, Utilities utilities) {
        this.userService = userService;
        this.utilities = utilities;
    }

    private final UserService userService;
    private final Utilities utilities;

    /**
     * Login metodu giriş yapmak için gereken sayfayı döndürür
     * @param user
     * @return Thymeleaf Page
     */
    @GetMapping("/login")
    public String login(Principal user) {
        if (user != null) {
            return "redirect:/dashboard";
        }
        return "auth/login";
    }

    /**
     * Register metodu kayıt olmak için gereken sayfayı döndürür
     * @param model
     * @return Thymeleaf Page
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userObj", new User());
        return "auth/register";
    }

    /**
     * ProcessRegister metodu kayıt olmak için kullanılan fonksiyondur
     * @param user
     * @param request
     * @param model
     * @return Thymeleaf Page
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("userObj") User user, HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {

        if (!Pattern.compile("^(.+)@(.+)$").matcher(user.getEmail()).matches()) {
            model.addAttribute("isRegisterSuccess", false);
            return "auth/register";
        }

        if (userService.save(user, utilities.getSiteURL(request))){
            model.addAttribute("isRegisterSuccess", true);
            return "auth/login";
        } else {
            model.addAttribute("isRegisterSuccess", false);
            return "auth/register";
        }
    }

    /**
     * VerifyUser metodu email aktivasyonu için kullanılan fonksiyondur
     * @param code
     * @return Thymeleaf Page
     */
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "auth/verify_success";
        } else {
            return "auth/verify_fail";
        }
    }

    /**
     * ShowForgotPasswordForm metodu şifre sıfırlamak için "şifremi unuttum" sayfasını döndürür
     * @return Thymeleaf Page
     */
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "auth/forgot_password";
    }

    /**
     * ProcessForgotPassword metodu şifre sıfırlamak için kullanılan fonksiyondur
     * @param model
     * @param request
     * @return Thymeleaf Page
     */
    @PostMapping("/forgot_password")
    public String processForgotPassword(Model model, HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = utilities.getSiteURL(request) + "/reset_password?token=" + token;
            utilities.sendForgotPasswordMail(email, resetPasswordLink);
            model.addAttribute("isSendMailSuccess", true);

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "auth/forgot_password";
    }

    /**
     * ShowResetPasswordForm metodu yeni şifre tanımlamak için gereken sayfayı döndürür
     * @param token
     * @param model
     * @return Thymeleaf Page
     */
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.findByResetPasswordToken(token);
        model.addAttribute("token", token);

        return "auth/reset_password";
    }

    /**
     * ProcessResetPassword metodu yeni şifre tanımlamak için kullanılan fonksiyondur
     * @param token
     * @param password
     * @param model
     * @return Thymeleaf Page
     */
    @PostMapping("/reset_password")
    public String processResetPassword(
            @Param("token") String token,
            @Param("password") String password,
            Model model) {

        User user = userService.findByResetPasswordToken(token);

        if (user == null) {
            model.addAttribute("isResetPasswordSuccess", false);
            return "auth/reset_password";
        }

        System.out.println(password);
        userService.updatePassword(user, password);
        model.addAttribute("isResetPasswordSuccess", true);

        return "auth/login";
    }

    /**
     * ChangePassword metodu kullanıcıların ayarlar menüsünden parolalarını güncellemesi için kullanılan fonksiyondur.
     * @param oldPassword
     * @param newPassword
     * @param principal
     * @param model
     * @return Thymeleaf Page
     */
    @PostMapping("/change_password")
    public String changePassword(@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword, Principal principal, Model model) {
        if(userService.changePassword(principal, oldPassword, newPassword)){
            model.addAttribute("isChangePasswordSuccess", true);
            return "user/settings";
        }
        model.addAttribute("isChangePasswordSuccess", false);
        return "user/settings";
    }
}
