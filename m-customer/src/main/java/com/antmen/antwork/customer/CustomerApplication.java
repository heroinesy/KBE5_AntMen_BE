package com.antmen.antwork.customer;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = {
        "com.antmen.antwork.common.infra.repository",
        "com.antmen.antwork.customer.infra.repository"
})
@EntityScan(basePackages = {
        "com.antmen.antwork.common.domain",
        "com.antmen.antwork.customer.domain"
})
@ComponentScan(basePackages = {
        "com.antmen.antwork.common",
        "com.antmen.antwork.customer"
})
@SpringBootApplication
@EnableScheduling
public class CustomerApplication {

    public static void main(String[] args) {
        // .env 파일 로드
        Dotenv dotenv = Dotenv.configure()
                .directory("./")  // 루트 디렉토리의 .env 파일
                .ignoreIfMissing()
                .load();

        // 시스템 프로퍼티로 설정
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(CustomerApplication.class, args);
    }

}
