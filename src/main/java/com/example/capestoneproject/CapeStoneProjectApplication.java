package com.example.capestoneproject;

import com.example.capestoneproject.entity.User;
import com.example.capestoneproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class CapeStoneProjectApplication  {


    public static void main(String[] args) {
        SpringApplication.run(CapeStoneProjectApplication.class, args);
    }
    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.SaveUser(new User("vijay", "M", "vijaymk98430@gmail.com", "Vijay123@", "male", "26/03/2002", "user", "123123", true));
        };
    }

}
