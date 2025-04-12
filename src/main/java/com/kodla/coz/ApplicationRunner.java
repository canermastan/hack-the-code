package com.kodla.coz;

import com.kodla.coz.utility.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * properties dosyasında tanımlanan profile'a göre mail gönderme işlemi yapılıyor.
 * Eğer profil "send-mail" iken çalıştırılır ise utilities.sendAnyMail(); içerisindeki tanımlı epostaya mail gönderir
 * Eğer profil "send-mail-to-all-users" ise utilities.sendAnyMailToAllUsers(); ile tüm kullanıcılara mail gönderilir
 */
// TODO: Bu sistemin daha iyi hale getirilmesi lazım
@Component
@Profile({"send-mail", "send-mail-to-all-users"})
public class ApplicationRunner implements CommandLineRunner {
    private final Utilities utilities;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);
    private final Environment environment;

    public ApplicationRunner(Utilities utilities, Environment environment) {
        this.utilities = utilities;
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        String activeProfile = environment.getActiveProfiles()[0]; // Assuming only one profile is active
        // Use activeProfile as needed
        if (activeProfile.equals("send-mail")) {
            utilities.sendAnyMail();
            logger.info("Mail sent.");
        } else if (activeProfile.equals("send-mail-to-all-users")) {
            utilities.sendAnyMailToAllUsers();
            logger.info("Mail sent to all users.");
        }
    }
}
