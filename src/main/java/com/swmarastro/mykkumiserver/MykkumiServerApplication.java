package com.swmarastro.mykkumiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.swmarastro.mykkumiserver")
public class MykkumiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MykkumiServerApplication.class, args);
    }
}