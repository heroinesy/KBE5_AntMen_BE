package com.antmen.antwork;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackages = "com.antmen.antwork.entity")
public class AntMenApplication {

	private static String port;
	private static String dockerUsername;
	private static String imageName;
	private static String mysqlUsername;
	private static String mysqlPassword;
	private static String springDatabaseUrl;
	
	@Value("${server.port}")
	public void setPort(String port) {
		AntMenApplication.port = port;
	}

	@Value("${my.docker.username}")
	public void setDockerUsername(String dockerUsername) {
		AntMenApplication.dockerUsername = dockerUsername;
	}

	@Value("${my.docker.image}")
	public void setImageName(String imageName) {
		AntMenApplication.imageName = imageName;
	}

	@Value("${spring.datasource.username}")
	public void setMysqlUsername(String username) {
		AntMenApplication.mysqlUsername = username;
	}

	@Value("${spring.datasource.password}")
	public void setMysqlPassword(String password) {
		AntMenApplication.mysqlPassword = password;
	}

	@Value("${spring.datasource.url}")
	public void setSpringDatabaseUrl(String url) {
		AntMenApplication.springDatabaseUrl = url;
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(AntMenApplication.class);
		
		// .env 파일 로드
		Dotenv dotenv = Dotenv.configure()
			.ignoreIfMissing()
			.load();
		
		// .env 파일의 값을 Properties로 변환
		Properties properties = new Properties();
		dotenv.entries().forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));
		
		// Spring Environment에 Properties 추가
		application.setDefaultProperties(properties);
		
		application.run(args);
	}

}
