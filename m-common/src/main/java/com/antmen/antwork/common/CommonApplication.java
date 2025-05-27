package com.antmen.antwork.common;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonApplication {

	public static void main(String[] args) {
		// .env 파일 로드
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // 루트 디렉토리의 .env 파일
				.ignoreIfMissing()
				.load();

		// 시스템 프로퍼티로 설정
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		// 환경변수 값 출력 (확인용)
		System.out.println("MYSQL_USERNAME: " + System.getProperty("MYSQL_USERNAME"));
		System.out.println("MYSQL_PASSWORD: " + System.getProperty("MYSQL_PASSWORD"));
	

		SpringApplication.run(CommonApplication.class, args);
	}

}
