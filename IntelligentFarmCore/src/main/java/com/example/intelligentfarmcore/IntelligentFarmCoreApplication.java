package com.example.intelligentfarmcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class IntelligentFarmCoreApplication {

    @PostConstruct
    void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static void main(String[] args) {
        System.out.println("Starting IntelligentFarmCoreApplication...");
        // 加载.env文件
        System.out.println("Loading .env file...");
        Dotenv dotenv = Dotenv.load();
        // 将.env文件中的环境变量设置到系统环境变量中
        System.out.println("Setting environment variables...");
        dotenv.entries().forEach(entry -> {
            if (System.getenv(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
        
        System.out.println("Running SpringApplication...");
        SpringApplication.run(IntelligentFarmCoreApplication.class, args);
        System.out.println("Application started successfully");
    }

}