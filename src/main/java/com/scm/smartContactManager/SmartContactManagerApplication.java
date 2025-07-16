package com.scm.smartContactManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@ComponentScan(basePackages = "com.scm.smartContactManager")
public class SmartContactManagerApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing()
                .load();

        SpringApplication.run(SmartContactManagerApplication.class, args);
    }

}
