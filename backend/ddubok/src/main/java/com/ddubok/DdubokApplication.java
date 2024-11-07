package com.ddubok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DdubokApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdubokApplication.class, args);
    }

}
