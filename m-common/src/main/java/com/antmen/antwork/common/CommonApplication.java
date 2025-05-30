package com.antmen.antwork.common;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class CommonApplication {

	public static void main(String[] args) {
		// 🔍 간단한 ENV 디버깅
		System.out.println("📁 현재 디렉토리: " + System.getProperty("user.dir"));

		File envFile = new File("./.env");
		System.out.println("📄 .env 파일 존재: " + envFile.exists());
		if (envFile.exists()) {
			System.out.println("📍 .env 파일 위치: " + envFile.getAbsolutePath());
		}

		// .env 파일 로드
		Dotenv dotenv = Dotenv.configure()
                .directory("./") // 절대 경로로 변경
				.ignoreIfMissing()
				.load();

		System.out.println("📊 로드된 환경변수 개수: " + dotenv.entries().size());

		// JWT secret 확인
		String jwtSecret = dotenv.get("jwt.secret");
		System.out.println("🔑 jwt.secret: " + (jwtSecret != null ? "✅ 있음" : "❌ 없음"));

		// 시스템 프로퍼티로 설정
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(CommonApplication.class, args);
	}

}
