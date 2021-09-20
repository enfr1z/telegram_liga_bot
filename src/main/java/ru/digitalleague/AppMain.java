package ru.digitalleague;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class AppMain {
    public static void main(String[] args) {
        System.out.println("initialize bot");
        ApiContextInitializer.init();

        SpringApplication.run(AppMain.class, args);
        System.out.println("End initialize spring and bot!");
    }

}
