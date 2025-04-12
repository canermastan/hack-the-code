package com.kodla.coz.controller;

import com.kodla.coz.model.Task;
import com.kodla.coz.service.SolvedTaskService;
import com.kodla.coz.model.Compiler;
import com.kodla.coz.model.SolvedTask;
import com.kodla.coz.model.dtos.FilterQueryDto;
import com.kodla.coz.service.TaskCodeService;
import com.kodla.coz.service.TaskService;
import com.kodla.coz.service.UserService;
import com.kodla.coz.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {

    @Autowired
    public TaskController(TaskService taskService, TaskCodeService taskCodeService, Utilities utilities, SolvedTaskService solvedTaskService, UserService userService) {
        this.taskService = taskService;
        this.taskCodeService = taskCodeService;
        this.utilities = utilities;
        this.solvedTaskService = solvedTaskService;
        this.userService = userService;
    }

    private final TaskService taskService;

    private final TaskCodeService taskCodeService;
    private final Utilities utilities;

    private final SolvedTaskService solvedTaskService;

    private final UserService userService;

    /**
     * AlgorithmPage metodu Algoritma butonuna basıldıktan sonra çıkan menü sayfasını döndürür
     * @return Thymeleaf Page
     */
    @GetMapping("/algorithm")
    public String AlgorithmPage() {
        return "content/algorithm";
    }

    /**
     * AlgorithmList metodu seçilen dile ait görevleri listeleyen sayfayı döndürür
     * @param id
     * @param principal
     * @param model
     * @param filterQueryDto
     * @return Thymeleaf Page
     */
    @RequestMapping(value = "/algorithm/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String AlgorithmList(@PathVariable("id") Integer id, Principal principal, Model model, @ModelAttribute FilterQueryDto filterQueryDto) {
        String language = switch (id) {
            case 1 -> "Genel";
            case 2 -> "Python";
            case 3 -> "Java";
            case 4 -> "C#";
            case 5 -> "C";
            case 6 -> "C++";
            case 7 -> "JavaScript";
            case 8 -> "Go";
            case 9 -> "Php";
            default -> null;
        };
        List<Task> tasks = null;
        Integer userId = userService.findByEmail(principal.getName()).getId();

        /**
         * Bu if bloğu soruları filtrelemek için kullanıldı
         * @see FilterQueryDto
         */
        if (filterQueryDto.getDifficulty() != null && filterQueryDto.getSolved() != null) {
            if (filterQueryDto.getDifficulty().isEmpty() && filterQueryDto.getSolved().isEmpty()) {
                tasks = taskService.findWhereLanguageIdIs(id);
            } else if (!filterQueryDto.getSolved().isEmpty() && filterQueryDto.getDifficulty().isEmpty()) {
                for (String solved : filterQueryDto.getSolved()) {
                    if (solved.equals("true")) {
                        tasks = taskService.findWhereLanguageIdIsAndSolved(userId, id);
                        break;
                    } else if (solved.equals("false")) {
                        tasks = taskService.findWhereLanguageIdIsAndUnsolved(userId, id);
                        break;
                    }
                }
            } else if (filterQueryDto.getSolved().isEmpty() && !filterQueryDto.getDifficulty().isEmpty()) {
                for (String diff : filterQueryDto.getDifficulty()) {
                    tasks = taskService.findWhereLanguageIdIsAndDifficultyIs(id, filterQueryDto.getDifficulty());
                }
            } else {
                for (String solved : filterQueryDto.getSolved()) {
                    if (solved.equals("true")) {
                        tasks = taskService.findWhereLanguageIdIsAndDifficultyIsAndSolved(userId, id, filterQueryDto.getDifficulty());
                        break;
                    } else {
                        tasks = taskService.findWhereLanguageIdIsAndDifficultyIsAndUnsolved(userId, id, filterQueryDto.getDifficulty());
                        break;
                    }
                }
            }
        } else {
            tasks = taskService.findWhereLanguageIdIs(id);
        }


        model.addAttribute("filter", new FilterQueryDto());
        model.addAttribute("languageId", id);
        model.addAttribute("tasks", tasks);
        model.addAttribute("language", language);
        return "content/algorithm-list";
    }

    /**
     * ShowTask metodu seçilen göreve ait sayfayı döndürür
     * @param model
     * @param id
     * @param languageId
     * @param principal
     * @return Thymeleaf Page
     */
    @GetMapping("/task/{id}")
    public String showTask(Model model, @PathVariable("id") Integer id, @RequestParam(value = "language", required = false) Optional<Integer> languageId, Principal principal) {
        taskService.findById(id).ifPresent(task -> {
            // if the question is solved get the code
            SolvedTask solvedTask = solvedTaskService.findSolvedTaskByTaskIdAndUserId(
                    task.getId(),
                    userService.findByEmail(principal.getName()).getId()
            );
                model.addAttribute("solvedTaskCode", solvedTask != null ? solvedTask.getCode() : "");
            model.addAttribute("isSolvedTask", solvedTask != null); // solvedTask != null ? true : false
            // get task
            model.addAttribute("task", task);
            // get task's language
            model.addAttribute("language", taskService.findTaskLanguage(task.getId()));

            languageId.ifPresentOrElse(
                    e -> model.addAttribute("taskCode", taskCodeService.findWhereTaskIdAndLanguageIdIs(task.getId(), e)),
                    () -> model.addAttribute("taskCode", taskCodeService.findWhereTaskIdAndLanguageIdIs(task.getId(), languageId.orElseThrow())
                    ));
        });
        return "content/task";
    }

    /**
     * Compiler metodu kodların derlenmesi için kullanılan fonksiyondur
     * @param compiler
     * @param principal
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/compile", produces = "application/json")
    public @ResponseBody String compiler(@RequestBody Compiler compiler, Principal principal) throws IOException {
        Integer userId = userService.findByEmail(principal.getName()).getId();

        return taskService.compile(compiler, userId);
    }
}
