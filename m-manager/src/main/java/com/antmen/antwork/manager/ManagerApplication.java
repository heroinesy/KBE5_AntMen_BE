package com.antmen.antwork.manager;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan(basePackages = {
                "com.antmen.antwork.common",
                "com.antmen.antwork.manager" })
public class ManagerApplication {

        public static void main(String[] args) {
                // .env 파일 로드
                String directory = findProjectRoot();

                // .env 파일 로드
                Dotenv dotenv = Dotenv.configure()
                                .directory(directory) // 절대 경로로 변경
                                .ignoreIfMissing()
                                .load();

                // 시스템 프로퍼티로 설정
                dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

                SpringApplication.run(ManagerApplication.class, args);
        }

        public static String findProjectRoot() {
                File current = new File(System.getProperty("user.dir"));

                while (current != null) {
                        // README.md 있으면 프로젝트 루트로 판단
                        if (new File(current, "README.md").exists()) {
                                return current.getAbsolutePath();
                        }
                        current = current.getParentFile();
                }

                // README.md 찾지 못한 경우 현재 디렉토리 반환
                return System.getProperty("user.dir");
        }

}
