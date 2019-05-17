package com.iamsid.geek.vibes.springschedule.controller;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.iamsid.geek.vibes.springschedule.service.ApplicationService;


@RestController
@RequestMapping(value="/vibes/api/scheduler")
public class AppController {
	
	@Autowired
	private ApplicationService applicationService;
	
	@GetMapping(value="/ping",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String,String> ping(){
		return ImmutableMap.of("message","Ping Success !!!");
	} 
	
	@GetMapping(value="/list",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Set<ScheduledTask> listSchedules(){
		return this.applicationService.listSchedules();
	} 
	@GetMapping(value="/stop",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String,String> stopTask(@RequestParam String taskName,@RequestParam(required=false,defaultValue="false") Boolean stopCurrentFlag){
		this.applicationService.stopSchedule(taskName,stopCurrentFlag);
		return ImmutableMap.of("message","Task Stopped");
	}
	@GetMapping(value="/start",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String, String> startTask(@RequestParam String taskName,
			@RequestParam(required = false, defaultValue = "-1") Long initialDelayValue,
			@RequestParam(required = false, defaultValue = "-1") Long fixedDelayValue) throws NoSuchMethodException {
		this.applicationService.startSchedule(taskName, initialDelayValue, fixedDelayValue);
		return ImmutableMap.of("message","Task Started");
	}
	
	@GetMapping(value="/start/all",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String, ScheduledFuture<?>> startAllTask(){
		return this.applicationService.startAll();
		
	}
	
	@GetMapping(value="/stop/all",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String, ScheduledFuture<?>> stopAllTask(){
		return this.applicationService.stopAll();
		
	}
}
