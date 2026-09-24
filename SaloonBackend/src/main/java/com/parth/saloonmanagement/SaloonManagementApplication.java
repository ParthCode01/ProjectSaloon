package com.parth.saloonmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class SaloonManagementApplication {

    public static void main(String[] args) {

        SpringApplication.run(SaloonManagementApplication.class, args);
    }

}
