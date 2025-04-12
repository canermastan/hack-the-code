package com.kodla.coz.utility;

import com.kodla.coz.model.User;
import com.kodla.coz.repository.UserRepository;
import com.kodla.coz.config.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class AnnotationAdvice {
    private final UserRepository userRepository;

    public AnnotationAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *  send to template engine the current user
     *  access it with ${user}
     * @return User
     */
    @ModelAttribute("user")
    public User getCurrentUser(){
        try{
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
