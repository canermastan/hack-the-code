package com.kodla.coz.controller;

import com.kodla.coz.model.Task;
import com.kodla.coz.model.TaskCode;
import com.kodla.coz.repository.AdminRepository;
import com.kodla.coz.service.TaskCodeService;
import com.kodla.coz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    public AdminController(TaskService taskService, AdminRepository adminRepository, TaskCodeService taskCodeService) {
        this.taskService = taskService;
        this.adminRepository = adminRepository;
        this.taskCodeService = taskCodeService;
    }

    private final TaskService taskService;
    private final AdminRepository adminRepository;
    private final TaskCodeService taskCodeService;

    /**
     * TaskList metodu tüm görevleri listeler
     * @param model
     * @return Thymeleaf Page
     */
    @GetMapping("/task/list")
    public String taskList(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "admin/task-list";
    }

    /**
     * ShowTask metodu görevin kodlarının yazıldığı sayfayı döndürür
     * @param model
     * @param id
     * @return Thymeleaf Page
     */
    @GetMapping("/task/{id}")
    public String showTask(Model model, @PathVariable("id") Integer id) {

        taskService.findById(id).ifPresent(task -> {
            model.addAttribute("task", task);
            model.addAttribute("language", taskService.findTaskLanguage(task.getId()));
        });

        model.addAttribute("taskCodes", taskCodeService.findTaskCodesByTaskId(id));
        return "admin/task-codes";
    }

    /**
     * DeleteTask metodu görevi silmek için kullanılan metottur.
     * @param id
     * @return redirect
     */
    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable("id") Integer id) {
        taskService.deleteById(id);
        return "redirect:/admin/task/list";
    }

    /**
     * DeleteTaskCode metodu görevin kodlarını silmek için kullanılan metottur.
     * @param id
     * @return redirect
     */
    @PostMapping("/task/codes/delete/{id}")
    public String deleteTaskCode(@PathVariable("id") Integer id) {
        taskCodeService.deleteById(id);
        return "redirect:/admin/task/list";
    }

    /**
     * UpdateTask metodu görevi güncellemek için gereken sayfayı döndürür
     * @param model
     * @param id
     * @return Thymeleaf Page
     */
    @GetMapping("/task/update/{id}")
    public String updateTask(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("task", taskService.findById(id));
        return "admin/edit-task";
    }

    /**
     * ProcessUpdateTask metodu görevi güncelleyen fonksiyondur
     * @param task
     * @param languageId
     * @return redirect
     */
    @PostMapping("/task/update")
    public String processUpdateTask(@ModelAttribute("task") Task task, @RequestParam Integer languageId) {
        Task taskDb = taskService.findById(task.getId()).orElse(null);
        if (taskDb == null) {
            return "redirect:/admin/task/list";
        }

        taskDb.setId(task.getId());
        taskDb.setDifficulty(task.getDifficulty());
        taskDb.setScore(task.getScore());
        taskDb.setTitle(task.getTitle());
        taskDb.setRating(task.getRating());
        taskDb.setContent(task.getContent());
        taskDb.setSuccessRate(task.getSuccessRate());
        taskDb.setLanguage(adminRepository.findLanguageById(languageId));

        taskService.save(taskDb);
        return "redirect:/admin/task/list";
    }

    /**
     * UpdateTaskCodes metodu görevin kodlaarını güncellemek için gereken sayfayo döndürür
     * @param model
     * @param taskId
     * @param languageId
     * @return Thymeleaf Page
     */
    @GetMapping("/task/codes/update/{taskId}/{languageId}")
    public String updateTaskCodes(Model model, @PathVariable("taskId") Integer taskId, @PathVariable("languageId") Integer languageId) {
        model.addAttribute("task", taskService.findById(taskId));
        model.addAttribute("taskCode", taskCodeService.findByTaskIdAndLanguageId(taskId, languageId));
        return "admin/edit-task-code";
    }

    /**
     * ProcessUpdateCodes metodu görevi kodlarını güncelleyen fonksiyondur
     * @param taskCode
     * @return redirect
     */
    @PostMapping("/task/codes/update")
    public String processUpdateCodes(@ModelAttribute("taskCode") TaskCode taskCode) {
        taskCodeService.save(taskCode);
        return "redirect:/admin/task/" + taskCode.getTaskId();
    }

    /**
     * NewTaskCode metodu göreve yeni kod eklemek için gereken sayfayı döndürür
     * @param model
     * @param id
     * @return Thymeleaf Page
     */
    @GetMapping("/taskcode/new/{id}")
    public String newTaskCode(Model model, @PathVariable("id") int id) {
        TaskCode taskCode = new TaskCode();
        taskCode.setTaskId(id);

        model.addAttribute("taskCode", taskCode);
        return "admin/new-taskcode";
    }

    /**
     * NewTaskCode metodu göreve yeni kod eklemek için kullanılan fonksiyondur
     * @param taskCode
     * @return redirect
     */
    @PostMapping("/taskcode/new")
    public String newTaskCode(@ModelAttribute("taskCode") TaskCode taskCode) {
        taskCodeService.save(taskCode);
        return "redirect:/admin/task/list";
    }

    /**
     * NewTask metodu yeni bir görev eklemek için gereken sayfayı döndürür
     * @param model
     * @return Thymeleaf Page
     */
    @GetMapping("/task/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        return "admin/new-task";
    }

    /**
     * NewTask metodu yeni bir görev eklemek için kullanılan fonksiyondur
     * @param task
     * @param languageId
     * @return redirect
     */
    @PostMapping("/task/new")
    public String newTask(@ModelAttribute("task") Task task, @RequestParam("languageId") Integer languageId) {
        Task taskDb = taskService.save(task);
        if (taskDb == null) {
            return "redirect:/admin/new";
        }
        adminRepository.setLanguageOfTask(taskDb.getId(), languageId);
        return "redirect:/algorithm/" + languageId;
    }
}
