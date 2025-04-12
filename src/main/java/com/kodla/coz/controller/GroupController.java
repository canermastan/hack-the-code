package com.kodla.coz.controller;

import com.kodla.coz.model.Group;
import com.kodla.coz.model.User;
import com.kodla.coz.service.GroupService;
import com.kodla.coz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/group")
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createGroup(Model model){
        Group group = new Group();
        group.setJoinCode(UUID.randomUUID().toString().substring(0,8));
        model.addAttribute("group", group);
        return "group/create-group";
    }

    @PostMapping("/create")
    public String processCreateGroup(@ModelAttribute("group") Group group, Principal principal){
        User user = userService.findByEmail(principal.getName());
        group.setAdminId(user.getId());

        List<User> users = new ArrayList<>();
        users.add(user);
        group.setUsers(users);

        Group groupDb = groupService.save(group);

        return "redirect:/group/"+groupDb.getId();
    }

    @GetMapping("/{id}")
    public String myGroup(@PathVariable("id") Integer groupId, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());

        if (!groupService.isUserInGroup(groupId, user)) {
            return "error/403";
        }

        Group group = groupService.findById(groupId).orElseThrow();

        double completionPercentage = groupService.calculateTaskCompletionPercentage(group);
        long completedTasks = groupService.getCompletedTaskCount(group);
        int totalTasks = groupService.getTotalTaskCount(group);

        model.addAttribute("group", group);
        model.addAttribute("task", group.getTasks());
        model.addAttribute("completionPercentage", completionPercentage);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("totalTasks", totalTasks);
        return "group/group-details";
    }
    @PostMapping("/join")
    public String joinGroup(@RequestParam("joinCode") String joinCode, Model model, RedirectAttributes redirectAttributes, Principal principal) {

        User user = userService.findByEmail(principal.getName());

        if (user != null) {
            boolean success = groupService.joinGroup(joinCode, user);

            if (success) {
                Group group = groupService.findByJoinCode(joinCode).get();
                redirectAttributes.addFlashAttribute("message", "Başarılı bir şekilde gruba kayıt olundu");
                return "redirect:/group/" + group.getId();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Geçersiz grup kodu veya grup bulunamadı veya bu gruba zaten üyesiniz.");
                return "redirect:/groups";
            }
        } else {
            return "redirect:/login";
        }
    }
}