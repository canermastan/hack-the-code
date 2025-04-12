package com.kodla.coz;

import com.kodla.coz.model.*;
import com.kodla.coz.repository.TaskCodeRepository;
import com.kodla.coz.repository.TaskRepository;
import com.kodla.coz.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Test amaçlı 2 adet kullanıcı ekler ve bir adet soru ekler
 */
//@Component
//@Profile("dev")
public class DataLoader implements ApplicationRunner {

    public DataLoader(TaskRepository taskRepository, UserRepository userRepository, TaskCodeRepository taskCodeRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskCodeRepository = taskCodeRepository;
    }
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskCodeRepository taskCodeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Task task = new Task();
        User user = new User();

        user.setPassword("$2a$10$6I.Vcpf8oYkEYySlOp5MH.TuuZ8rTVKVM99VqdK6ZEw8p4iTjBO0q"); // pw -> 123456
        user.setEmail("test@gmail.com");
        user.setEnabled(true);
        user.setId(1);
        user.setLevel(1);
        user.setNickname("testnickname");
        user.setFullName("Test Test");
        user.setBiography("Test biography.");
        user.setTotalScore(70);

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, "USER"));
        roles.add(new Role(2, "ADMIN"));

        user.setRoles(roles);
        userRepository.save(user);


        User user2 = new User();

        user2.setPassword("$2a$10$6I.Vcpf8oYkEYySlOp5MH.TuuZ8rTVKVM99VqdK6ZEw8p4iTjBO0q"); // pw -> 123456
        user2.setEmail("jcanermastan@gmail.com");
        user2.setEnabled(true);
        user2.setId(2);
        user2.setLevel(1);
        user2.setNickname("canermastan");
        user2.setFullName("Caner Mastan");
        user2.setBiography("Caner Mastan Biography.");
        user2.setTotalScore(90);

        user2.setRoles(roles);
        userRepository.save(user2);


        task.setTitle("Üçgenin Çevresi");
        task.setId(1);
        task.setRating(5.0F);
        task.setScore(20);
        task.setSuccessRate(100.0F);
        task.setDifficulty("Kolay");

        TaskCode taskCode = new TaskCode();

        task.setContent("""
                <p><strong>&Uuml;&ccedil;genin &ccedil;evresi, &uuml;&ccedil;genin kenar uzunluklarının toplamı ile bulunur.</strong></p>
                                
                <p><em>&Ouml;rneğin:</em></p>
                                
                <ol>
                                
                <li><em>Kenar: A</em></li>
                                
                <li><em>Kenar: B</em></li>
                                
                <li><em>Kenar: C</em></li>
                                
                </ol>
                                
                <p><em>ise, &Ccedil;evre = A + B + C 'dir.</em></p>
                                
                <p>Buna g&ouml;re kenar uzunluklarından &uuml;&ccedil;genin &ccedil;evresini bulan ve ekrana yazdıran programı kodlayınız.</p>
                """);
        Language language = new Language();
        language.setId(2);
        language.setName("Python");
        task.setLanguage(language);

        taskCode.setCode("""            
                import pytest

                def cevre(a, b, c):
                    # burayı doldur

                sonuc = cevre(3, 4, 5)\s
                print(sonuc) # beklenen : 12
                """);

        taskCode.setTestCode("""
                def test_cevre():
                    assert cevre(3, 4, 5) == 12
                    assert cevre(6, 8, 10) == 24
                    assert cevre(12, 16, 20) == 48
                """);

        taskCode.setTaskId(task.getId());
        taskCode.setLanguageId(2);

        taskRepository.save(task);
        taskCodeRepository.save(taskCode);
    }
}
