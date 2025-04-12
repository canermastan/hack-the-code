package com.kodla.coz.service;

import com.kodla.coz.model.Task;
import com.kodla.coz.model.Compiler;
import com.kodla.coz.model.SolvedTask;
import com.kodla.coz.model.TaskCode;
import com.kodla.coz.repository.TaskRepository;
import com.kodla.coz.utility.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskCodeService taskCodeService, Utilities utilities, SolvedTaskService solvedTaskService, UserService userService) {
        this.taskRepository = taskRepository;
        this.taskCodeService = taskCodeService;
        this.utilities = utilities;
        this.solvedTaskService = solvedTaskService;
        this.userService = userService;
    }

    private final TaskRepository taskRepository;
    private final TaskCodeService taskCodeService;
    private final Utilities utilities;
    private final SolvedTaskService solvedTaskService;
    private final UserService userService;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task save(Task task) {
        if (task.getScore() <= 100) { // FIXME neden score kontrol ediliyor?
            task.setRating(5F); // FIXME burayı kaldırmak lazım.
            return taskRepository.save(task);
        }
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task update(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> findWhereLanguageIdIs(Integer id) {
        return taskRepository.findWhereLanguageIdIsOrderByScoreAsc(id);
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    @Override
    public String findTaskLanguage(Integer taskId) {
        return taskRepository.findTaskLanguage(taskId);
    }

    /**
     * Kodun doğru olup olmadığını bulmak için output'u kontrol et
     * Algoritmaların test kodları yazılırken ekrana PASSED veya FAILURE yazdırılır o yüzden bu şekilde kontrol yapılıyor
     * Outputta PASSED bulunuyorsa algoritma doğru çalışıyordur
     * @param language
     * @param output
     * @return
     */
    @Override
    public boolean isPassedTest(String language, String output) {
        if (!output.contains("FAILURE") && output.contains("PASSED")) {
            return true;
        }
        return false;
    }

    @Override
    public void addScoreToUser(int score, Integer userId) {
        taskRepository.addScoreToUser(score, userId);
    }

    @Override
    public List<Task> findWhereLanguageIdIsAndDifficultyIs(Integer id, List<String> difficulties) {
        return taskRepository.findWhereLanguageIdIsAndDifficultyIs(id, difficulties);
    }

    @Override
    public List<Task> findWhereLanguageIdIsAndDifficultyIsAndSolved(Integer userId, Integer languageId, List<String> difficulties) {
        return taskRepository.findWhereLanguageIdIsAndDifficultyIsAndSolved(userId, languageId, difficulties);
    }

    @Override
    public List<Task> findWhereLanguageIdIsAndDifficultyIsAndUnsolved(Integer userId, Integer languageId, List<String> difficulties) {
        return taskRepository.findWhereLanguageIdIsAndDifficultyIsAndUnsolved(userId, languageId, difficulties);
    }


    @Override
    public List<Task> findWhereLanguageIdIsAndUnsolved(Integer userId, Integer languageId) {
        return taskRepository.findWhereLanguageIdIsAndUnsolved(userId, languageId);
    }

    @Override
    public List<Task> findWhereLanguageIdIsAndSolved(Integer userId, Integer languageId) {
        return taskRepository.findWhereLanguageIdIsAndSolved(userId, languageId);
    }

    /**
     * Compile metodu kullanıcıların kodları ile test kodlarının birleştirildiği, kodların çalıştırıldığı, doğruluğunun kontrol edildiği, puan eklemesi yapıldığı, kullanıcıya geri bildirim yapıldığı metottur.
     * <p>Projedeki en önemli metot bu metottur.
     * @param compiler
     * @param userId
     * @return String
     * @throws IOException
     */
    // TODO: Bu metodu daha temiz hale getir
    @Override
    public String compile(Compiler compiler, int userId) throws IOException {

        String fileName;

        switch (compiler.getLanguage()) {
            case "c#":
                fileName = "KodlaCoz";
                break;
            case "java":
                fileName = "Main";
                break;
            default:
                fileName = utilities.generateRandomStringWithUUID();
        }

        if (compiler.getLanguage() == null) {
            return "Lütfen Dili Seçiniz.";
        }

        // set file name according to language
        File file = switch (compiler.getLanguage()) {
            case "javascript" -> new File(fileName + ".js");
            case "c", "c++" -> new File(fileName + ".cpp");
            case "python" -> new File(fileName + ".py");
            case "java" -> new File(fileName + ".java");
            case "go" -> new File(fileName + ".go");
            case "php" -> new File(fileName + ".php");
            case "c#" -> new File(fileName + ".cs");
            default -> new File(fileName + "." + compiler.getLanguage());
        };

        String output = null;

        if (file.createNewFile()) {
            // write Code to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(compiler.getCode());
            writer.close();

            // compile and run code according to language
            switch (compiler.getLanguage()) {
                case "go":
                    // add Task's testCode to code file
                    addUnitTestCode(compiler, file);
                    // create a go module
                    String tempOutputGo = utilities.compile("go run " + fileName + ".go");
                    // check if the code is correct
                    if (isPassedTest(compiler.getLanguage(), tempOutputGo)) { // if passed test
                        int score = findById(compiler.getTaskId()).get().getScore();

                        if (solvedTaskService.isSolved(compiler.getTaskId(), userId)) {
                            output = "Kodunuz doğru ancak daha önce çözdüğünüz bir soruyu tekrar çözdünüz. Puan kazanamazsınız.";
                            break;
                        }

                        output = "TESTİ GEÇTİNİZ! " + "+" + score + " PUAN KAZANDINIZ!";

                        // add score to user and set level
                        addScoreAndSetLevel(compiler.getTaskId(), userId);
                        updateSuccessRate(compiler.getTaskId());
                        // save solved task to database with user_id and task_id and code
                        saveSolvedTask(compiler.getTaskId(), compiler.getCode(), userId);
                    } else {
                        output = "Test başarısız oldu. Lütfen kodunuzu kontrol edin.";
                    }
                    break;
                // if you use linux -> "python3" || windows -> "python"
                case "python":
                    // add Task's testCode to code file
                    addUnitTestCode(compiler, file);
                    // run unit test and get output
                    String tempOutputPy = utilities.compile("python3 " + fileName + ".py");
                    if (isPassedTest(compiler.getLanguage(), tempOutputPy)) {
                        int score = findById(compiler.getTaskId()).get().getScore();

                        if (solvedTaskService.isSolved(compiler.getTaskId(), userId)) {
                            output = "Kodunuz doğru ancak daha önce çözdüğünüz bir soruyu tekrar çözdünüz. Puan kazanamazsınız.";
                            break;
                        }

                        output = "TESTİ GEÇTİNİZ! " + "+" + score + " PUAN KAZANDINIZ!";

                        // add score to user and set level
                        addScoreAndSetLevel(compiler.getTaskId(), userId);
                        updateSuccessRate(compiler.getTaskId());
                        saveSolvedTask(compiler.getTaskId(), compiler.getCode(), userId);
                    } else {
                        output = "Test başarısız oldu. Lütfen kodunuzu kontrol edin.";
                    }
                    break;
                case "java": {
                    addUnitTestCode(compiler, file);

                    Runtime.getRuntime().exec("javac " + fileName + ".java");
                    String tempOutputJava = utilities.compile("java " + fileName);

                    if (isPassedTest(compiler.getLanguage(), tempOutputJava)) {
                        int score = findById(compiler.getTaskId()).get().getScore();

                        if (solvedTaskService.isSolved(compiler.getTaskId(), userId)) {
                            output = "Kodunuz doğru ancak daha önce çözdüğünüz bir soruyu tekrar çözdünüz. Puan kazanamazsınız.";
                            break;
                        }
                        output = "TESTİ GEÇTİNİZ! " + "+" + score + " PUAN KAZANDINIZ!";

                        // add score to user and set level
                        addScoreAndSetLevel(compiler.getTaskId(), userId);
                        updateSuccessRate(compiler.getTaskId());

                        saveSolvedTask(compiler.getTaskId(), compiler.getCode(), userId);
                    }
                    break;
                }
                case "javascript":

                    addUnitTestCode(compiler, file);

                    String tempOutputJs = utilities.compile("node " + fileName + ".js");

                    if (isPassedTest(compiler.getLanguage(), tempOutputJs)) {
                        int score = findById(compiler.getTaskId()).get().getScore();

                        if (solvedTaskService.isSolved(compiler.getTaskId(), userId)) {
                            output = "Kodunuz doğru ancak daha önce çözdüğünüz bir soruyu tekrar çözdünüz. Puan kazanamazsınız.";
                            break;
                        }

                        output = "TESTİ GEÇTİNİZ! " + "+" + score + " PUAN KAZANDINIZ!";

                        // add score to user and set level
                        addScoreAndSetLevel(compiler.getTaskId(), userId);
                        updateSuccessRate(compiler.getTaskId());
                        saveSolvedTask(compiler.getTaskId(), compiler.getCode(), userId);

                    } else {
                        output = "Test başarısız oldu. Lütfen kodunuzu kontrol edin.";
                    }

                    break;
                case "c":
                case "c++": {
                    addUnitTestCode(compiler, file);
                    Process process = Runtime.getRuntime().exec("g++ " + fileName + ".cpp -o " + fileName);
                    try {
                        process.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String tempOutputCandCpp = utilities.compile("./" + fileName);

                    if (isPassedTest(compiler.getLanguage(), tempOutputCandCpp)) {
                        int score = findById(compiler.getTaskId()).get().getScore();

                        if (solvedTaskService.isSolved(compiler.getTaskId(), userId)) {
                            output = "Kodunuz doğru ancak daha önce çözdüğünüz bir soruyu tekrar çözdünüz. Puan kazanamazsınız.";
                            break;
                        }

                        output = "TESTİ GEÇTİNİZ! " + "+" + score + " PUAN KAZANDINIZ!";

                        addScoreAndSetLevel(compiler.getTaskId(), userId);
                        updateSuccessRate(compiler.getTaskId());
                        saveSolvedTask(compiler.getTaskId(), compiler.getCode(), userId);
                    } else {
                        output = "Test başarısız oldu. Lütfen kodunuzu kontrol edin.";
                    }

                    break;
                }
                case "php":
                    addUnitTestCode(compiler, file);

                    String tempOutputPhp = utilities.compile("php " + fileName + ".php");

                    if (isPassedTest(compiler.getLanguage(), tempOutputPhp)) {
                        int score = findById(compiler.getTaskId()).get().getScore();

                        if (solvedTaskService.isSolved(compiler.getTaskId(), userId)) {
                            output = "Kodunuz doğru ancak daha önce çözdüğünüz bir soruyu tekrar çözdünüz. Puan kazanamazsınız.";
                            break;
                        }

                        output = "TESTİ GEÇTİNİZ! " + "+" + score + " PUAN KAZANDINIZ!";

                        // add score to user and set level
                        addScoreAndSetLevel(compiler.getTaskId(), userId);
                        updateSuccessRate(compiler.getTaskId());

                        saveSolvedTask(compiler.getTaskId(), compiler.getCode(), userId);
                    } else {
                        output = "Test başarısız oldu. Lütfen kodunuzu kontrol edin.";
                    }
                    break;
                case "c#":
                    addUnitTestCode(compiler, file);

                    Runtime.getRuntime().exec("mcs -out:" + fileName + ".exe " + fileName + ".cs");
                    String tempOutputCSharp = utilities.compile("mono " + fileName + ".exe");

                    if (isPassedTest(compiler.getLanguage(), tempOutputCSharp)) {
                        int score = findById(compiler.getTaskId()).get().getScore();

                        if (solvedTaskService.isSolved(compiler.getTaskId(), userId)) {
                            output = "Kodunuz doğru ancak daha önce çözdüğünüz bir soruyu tekrar çözdünüz. Puan kazanamazsınız.";
                            break;
                        }
                        output = "TESTİ GEÇTİNİZ! " + "+" + score + " PUAN KAZANDINIZ!";

                        // add score to user and set level
                        addScoreAndSetLevel(compiler.getTaskId(), userId);
                        updateSuccessRate(compiler.getTaskId());

                        saveSolvedTask(compiler.getTaskId(), compiler.getCode(), userId);
                    }
                    break;
            }

        }

        if (file.delete()) {
            assert output != null;
            // delete compiled file
            Runtime.getRuntime().exec("rm " + fileName);
            return output.replace("\n", "<br>");
        }
        return "HATA! Lütfen bize bildirin!";
    }

    private void updateSuccessRate(Integer taskId) {
        int solvedCount = solvedTaskService.countSolvedTasksByTaskId(taskId);

        if (solvedCount == 0) {
            solvedCount = 1;
        }

        int totalCount = solvedTaskService.countTotalSolvedTasks();

        String formattedSuccessRate = String.format("%.2f", (float) solvedCount / totalCount * 100);

        Task task = findById(taskId).orElseThrow();
        task.setSuccessRate(Float.parseFloat(formattedSuccessRate));
        save(task);
    }

    private void addScoreAndSetLevel(int taskId, Integer userId) {
        Task task = findById(taskId).orElseThrow();

        addScoreToUser(task.getScore(), userId);
        userService.setLevelById(userId);

    }

    private void addUnitTestCode(Compiler compiler, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

        int languageId = 0;
        switch (compiler.getLanguage()) {
            case "python":
                languageId = 2;
                break;
            case "java":
                languageId = 3;
                break;
            case "c#":
                languageId = 4;
                break;
            case "c":
                languageId = 5;
                break;
            case "c++":
                languageId = 6;
                break;
            case "javascript":
                languageId = 7;
                break;
            case "go":
                languageId = 8;
                break;
            case "php":
                languageId = 9;
                break;
        }

        // Add Unit Test Code
        TaskCode taskCode = taskCodeService.findWhereTaskIdAndLanguageIdIs(compiler.getTaskId(), languageId);

        writer.append("\n").append(taskCode.getTestCode());
        writer.close();
    }

    private void saveSolvedTask(Integer taskId, String code, Integer userId) {
        SolvedTask solvedTask = new SolvedTask();
        solvedTask.setTaskId(taskId);
        solvedTask.setCode(code);
        solvedTask.setUserId(userId);
        solvedTaskService.save(solvedTask);
    }
}
