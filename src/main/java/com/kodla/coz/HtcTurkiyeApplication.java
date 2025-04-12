package com.kodla.coz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "MyAuditorAware")
@SpringBootApplication
public class HtcTurkiyeApplication {
    public static void main(String[] args) {
        SpringApplication.run(HtcTurkiyeApplication.class, args);
    }
}
