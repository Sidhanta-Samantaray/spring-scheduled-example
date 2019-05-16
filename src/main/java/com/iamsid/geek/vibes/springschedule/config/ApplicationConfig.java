package com.iamsid.geek.vibes.springschedule.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
public class ApplicationConfig {
	
	@Bean
	public ScheduledThreadPoolExecutor scheduler() {
		ScheduledThreadPoolExecutor scheduler= new ScheduledThreadPoolExecutor(1);
		scheduler.setThreadFactory(new ThreadFactoryBuilder()
				.setNameFormat("app_scheduler")
				.build());
		scheduler.setRemoveOnCancelPolicy(true);
		return scheduler;
	}
}
