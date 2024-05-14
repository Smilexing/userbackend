package com.smile.userbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.smile.userbackend.mapper")
public class UserbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserbackendApplication.class, args);
    }

}
