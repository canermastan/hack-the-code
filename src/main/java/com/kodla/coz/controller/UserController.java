package com.kodla.coz.controller;

import com.kodla.coz.model.User;
import com.kodla.coz.service.UserService;
import com.kodla.coz.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;
    // Model User attribute is defined in utility.AnnotationAdvice class

    /**
     * Dashboard sayfası kullanıcı giriş yaptıktan sonraki anasayfayı döndürür
     * @param model
     * @param principal
     * @return Thymeleaf Page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        // for user progress bar
        model.addAttribute("myLevel", userService.findLevelByEmail(principal.getName()));
        // leaderboard user list (top 5 user)
        List<User> users = userService.findLeaderboardUsers();
        // users.forEach(e -> System.out.println(e.getSolvedTaskCount()));
        model.addAttribute("leaderboardUsers", userService.findLeaderboardUsers());
        return "user/dashboard";
    }

    /**
     * Profile metodu kullanıcının kendi profilini görüntüleyeceği sayfayı döndürür
     * @param model
     * @param principal
     * @return Thymeleaf Page
     */
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("countSolvedTask", userService.countBySolvedTasks(userService.findByEmail(principal.getName()).getId()));
        return "user/profile";
    }

    /**
     * Settings metodu kullanıcının ayarlar menüsünü görüntüleyeceği sayfayı döndürür
     * @param principal
     * @param model
     * @return Thymeleaf Page
     */
    @GetMapping("/settings")
    public String settings(Principal principal, Model model) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        return "user/settings";
    }

    /**
     * AnotherUser metodu başka bir kullanıcının profilini görüntüleyen sayfayı döndürür
     * @param model
     * @param id
     * @return Thymeleaf Page
     */
    @GetMapping("/profile/{id}")
    public String anotherUser(Model model, @PathVariable("id") Integer id) {

        userService.findById(id).ifPresent(user -> {
            model.addAttribute("anotherUser", user);
            model.addAttribute("countSolvedTask", userService.countBySolvedTasks(user.getId()));
        });

        return userService.findById(id).isPresent() ? "user/someone-elses-profile" : "redirect:/profile";
    }

    /**
     * ChangeBiography metodu kullanıcının biography'sini güncelleyen fonksiyondur
     * @param biography
     * @param principal
     * @return
     */
    @PostMapping("/change-biography")
    public String changeBiography(@RequestParam String biography, Principal principal) {
        userService.updateBiographyByEmail(principal, biography);
        return "redirect:/settings";
    }

    /**
     * ChangeProfilePicture metodu kullanıcının profil fotoğrafını güncelleyen fonksiyondur
     * @param file
     * @param principal
     * @return redirect
     * @throws IOException
     */
    @PostMapping("/change-profile-picture")
    public String changeProfilePicture(@RequestParam("image") MultipartFile file, Principal principal) throws IOException {
        if (file.isEmpty()) {
            return "redirect:/settings";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Boşlukları alt çizgi ile değiştir
        fileName = fileName.replaceAll("\\s", "_");
        // Özel karakterleri kaldır veya değiştir
        fileName = fileName.replaceAll("[^a-zA-Z0-9_.-]", "");

        // check is file image?
        if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
            return "redirect:/settings";
        }

        User userDb = userService.setProfilePicture(principal, fileName);

        String uploadDir = "user-photos/" + userDb.getId();
        Utilities.saveFile(uploadDir, fileName, file);

        return "redirect:/settings";
    }

    @GetMapping("/groups")
    public String userGroups(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("userGroups", user.getGroups());
        return "group/user-groups";
    }

    /**
     * PrivacyPolicy metodu privacy policy sayfasını döndürür
     * @return
     */
    @GetMapping("/privacy-policy")
    public String privacyPolicy() {
        return "privacy-policy";
    }

}
