package com.Team12.HADBackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class HADBackEnd {

	public static void main(String[] args) {
    SpringApplication.run(HADBackEnd.class, args);
	}

}
