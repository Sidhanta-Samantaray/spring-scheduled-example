package com.iamsid.geek.vibes.springschedule.service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;



@Service
public class ApplicationService {

	private static final Log LOGGER = LogFactory.getLog(ApplicationService.class);

	@Autowired
	private ScheduledAnnotationBeanPostProcessor postProcessor;

	@Autowired
	private ScheduledThreadPoolExecutor scheduler;
	   
	private static Map<String,ScheduledFuture<?>> jobsMap = new HashMap<>();
    
	@Value("${application_scheduler_initial_delay}")
	private  String initialDelay;
	
	@Value("${application_scheduler_fixed_delay}")
	private  String fixedDelay;
	

	@Scheduled(initialDelayString="${application_scheduler_initial_delay}",fixedDelayString="${application_scheduler_initial_delay}")
	private void processOne() throws InterruptedException {

		String processName=Thread.currentThread().getName()+"-processOne";
		LOGGER.info(processName+" Started At:" + LocalDateTime.now());
		Thread.sleep(5000);
		LOGGER.info(processName+" Completed At:" + LocalDateTime.now());

	}
	
	@Scheduled(initialDelayString="${application_scheduler_initial_delay}",fixedDelayString="${application_scheduler_initial_delay}")
	private void processTwo() throws InterruptedException {
		
		String processName=Thread.currentThread().getName()+"-processTwo";
		LOGGER.info(processName+" Started At:" + LocalDateTime.now());
		Thread.sleep(10000);
		LOGGER.info(processName+" Completed At:" + LocalDateTime.now());

	}

	public Set<ScheduledTask> listSchedules() {
		Set<ScheduledTask> scheduledTasks = this.postProcessor.getScheduledTasks();
		LOGGER.info(scheduledTasks);
		return scheduledTasks;
	}

	public void stopSchedule(String methodName, boolean stopCurrentFlag) {
		try {
			ScheduledFuture<?> future = ApplicationService.jobsMap.get(methodName);
			if (future != null) {
				LOGGER.info("Stop Current Flag:" + Boolean.valueOf(stopCurrentFlag));
				future.cancel(stopCurrentFlag);
				LOGGER.info("Canceled " + methodName);

			}

			for (ScheduledTask task : this.postProcessor.getScheduledTasks()) {
				ScheduledMethodRunnable methodRunnable = (ScheduledMethodRunnable) task.getTask().getRunnable();
				if (methodRunnable.getMethod().getName().equalsIgnoreCase(methodName)) {
					this.scheduler.remove(methodRunnable);
					if (future == null)
						task.cancel();// Warning this will Interrupt the Current Running Task Too
					ApplicationService.jobsMap.put(methodName, null);
					break;
				}

			}

		} catch (Exception e) {
			LOGGER.error("Error Stopping "+methodName);
		}

	}
	
	
	public void startSchedule(String methodName,long initialDelayValue,long fixedDelayValue) throws NoSuchMethodException {
		
		initialDelayValue=(initialDelayValue<=0)?new Long(this.initialDelay):initialDelayValue;
		fixedDelayValue=(fixedDelayValue<=0)?new Long(this.fixedDelay):fixedDelayValue;
		
		for (ScheduledTask task : this.postProcessor.getScheduledTasks()) {
			 ScheduledMethodRunnable methodRunnable = (ScheduledMethodRunnable) task.getTask().getRunnable();
			if (methodRunnable.getMethod().getName().equalsIgnoreCase(methodName)) {
				LOGGER.info("Resume "+methodName);
				ScheduledFuture<?> future=this.scheduler.scheduleWithFixedDelay(methodRunnable, initialDelayValue,fixedDelayValue,TimeUnit.MILLISECONDS);
				ApplicationService.jobsMap.put(methodName,future);
				break;
			}

		}
		LOGGER.info("Started "+methodName+" with "+ initialDelayValue +" Initial Delay & "+ fixedDelayValue +" Fixed Delay");
	}
	
	
	public Map<String, ScheduledFuture<?>> stopAll() {
		
		for (ScheduledTask task : this.postProcessor.getScheduledTasks()) {
			ScheduledMethodRunnable methodRunnable = (ScheduledMethodRunnable) task.getTask().getRunnable();
			String methodName = methodRunnable.getMethod().getName();
			try {
				ScheduledFuture<?> future = ApplicationService.jobsMap.get(methodName);
				if (future == null) {
					task.cancel();
					
				} else {
					future.cancel(false);
				}
				ApplicationService.jobsMap.put(methodName, future);
				LOGGER.info("Stopped "+methodName);
			
			}catch (Exception e) {
				LOGGER.error(e.getMessage());
			
			}
			
		}
		return ApplicationService.jobsMap;
	}

	public Map<String, ScheduledFuture<?>> startAll() {
		
		for (ScheduledTask task : this.postProcessor.getScheduledTasks()) {
			ScheduledMethodRunnable methodRunnable = (ScheduledMethodRunnable) task.getTask().getRunnable();
			String methodName = methodRunnable.getMethod().getName();
			LOGGER.info("Resume " + methodName);
			ScheduledFuture<?> future = this.scheduler.scheduleWithFixedDelay(methodRunnable,
					new Long(this.initialDelay), new Long(this.fixedDelay), TimeUnit.MILLISECONDS);
			ApplicationService.jobsMap.put(methodName, future);

		}
		return ApplicationService.jobsMap;
	}
}
