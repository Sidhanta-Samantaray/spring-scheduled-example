package com.iamsid.geek.vibes.springschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringScheduleApplication.class, args);
	}

}
