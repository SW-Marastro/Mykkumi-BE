package com.swmarastro.mykkumiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MykkumiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MykkumiServerApplication.class, args);
    }
}