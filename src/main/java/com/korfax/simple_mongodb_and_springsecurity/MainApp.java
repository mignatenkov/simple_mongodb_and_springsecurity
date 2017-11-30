package com.korfax.simple_mongodb_and_springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Created by Korfax on 2017-11-30.
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

}
