package com.example.liu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.liu.mapper")
public class LiuMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiuMusicApplication.class, args);
    }

}

