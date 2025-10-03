package com.blink.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@EnableFeignClients
@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@EntityScan("com.blink.backend.persistence.entity.*")
@EnableJpaRepositories(basePackages = "com.blink.backend.persistence.repository")
public class BlinkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlinkBackendApplication.class, args);
    }

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}