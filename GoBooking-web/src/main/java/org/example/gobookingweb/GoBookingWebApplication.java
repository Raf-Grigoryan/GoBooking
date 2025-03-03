package org.example.gobookingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"org.example.gobookingweb", "org.example.gobookingcommon"})
@EntityScan(basePackages = "org/example/gobookingcommon/entity")
@EnableJpaRepositories(basePackages = {"org.example.gobookingcommon"})
@EnableAsync
public class GoBookingWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoBookingWebApplication.class, args);
    }

}
