package com.antmen.antwork;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class AntMenApplication {

	private static String port;
	private static String dockerUsername;
	private static String imageName;

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

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(AntMenApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("Server Port: " + port);
		System.out.println("Docker Username: " + dockerUsername);
		System.out.println("Docker Image: " + imageName);
	}
}
