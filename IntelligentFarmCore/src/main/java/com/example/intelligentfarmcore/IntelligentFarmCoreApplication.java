package com.example.intelligentfarmcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
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
        // 加载.env文件
        Dotenv dotenv = Dotenv.load();
        // 将.env文件中的环境变量设置到系统环境变量中
        dotenv.entries().forEach(entry -> {
            if (System.getenv(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
        
        SpringApplication.run(IntelligentFarmCoreApplication.class, args);
    }

}
