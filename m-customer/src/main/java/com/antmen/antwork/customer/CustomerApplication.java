package com.antmen.antwork.customer;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.antmen.antwork.customer",
        "com.antmen.antwork.common"  // common 모듈의 컴포넌트들을 스캔하도록 추가
})

public class CustomerApplication {

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

        SpringApplication.run(CustomerApplication.class, args);
    }

}
