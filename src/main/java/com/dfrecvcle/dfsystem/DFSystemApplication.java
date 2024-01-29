package com.dfrecvcle.dfsystem;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@MapperScan("com.ceos.livesystem.datasource.mappers")
@SpringBootApplication
@ServletComponentScan
public class DFSystemApplication implements CommandLineRunner {

//    @Autowired
//    private GameTalkService gameTalkService;

    public static void main(String[] args) {
        SpringApplication.run(DFSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        gameTalkService.createGameTalk();
    }
}