package com.antmen.antwork.admin;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = {
        "com.antmen.antwork.common.domain.entity",     // User
        "com.antmen.antwork.customer.domain.entity"    // ReviewSummary
})
@EnableJpaRepositories(basePackages = {
        "com.antmen.antwork.common.infra.repository",
        "com.antmen.antwork.common.infra.repository"
})
public class AdminApplication {

    public static void main(String[] args) {
        // .env 파일 로드
        Dotenv dotenv = Dotenv.configure()
                .directory("../")  // 루트 디렉토리의 .env 파일
                .ignoreIfMissing()
                .load();

        // 시스템 프로퍼티로 설정
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(AdminApplication.class, args);
    }

}
