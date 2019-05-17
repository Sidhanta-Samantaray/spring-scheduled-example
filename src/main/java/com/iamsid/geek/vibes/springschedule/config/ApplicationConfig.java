package com.iamsid.geek.vibes.springschedule.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
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
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.iamsid.geek.vibes.springschedule.controller"))
          .paths(PathSelectors.any())                    
          .build();                                           
    }
}
