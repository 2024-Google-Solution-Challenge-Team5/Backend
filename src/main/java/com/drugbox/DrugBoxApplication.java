package com.drugbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DrugBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrugBoxApplication.class, args);
    }

}
