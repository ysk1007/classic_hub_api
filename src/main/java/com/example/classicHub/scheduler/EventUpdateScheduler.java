package com.example.classicHub.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.classicHub.service.EventService;

@Component
public class EventUpdateScheduler {

	private EventService eventService;
	
	/** 생성자 **/
	public EventUpdateScheduler(EventService eventService) {
		this.eventService = eventService;
	}
	
	@Scheduled(cron = "0 0 3 * * *")
	public void eventUpdate() {
		eventService.eventUpdate();				// 티켓팅 사이트 크롤링
	}
}
