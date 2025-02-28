package org.example.gobookingrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.example.gobookingrest", "org.example.gobookingcommon"})
@ComponentScan(basePackages = {"org.example.gobookingrest", "org.example.gobookingcommon", "org.example.security"})
@EntityScan(basePackages = "org/example/gobookingcommon/entity")
@EnableJpaRepositories(basePackages = {"org.example.gobookingcommon"})


public class GoBookingRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoBookingRestApplication.class, args);
    }

}
