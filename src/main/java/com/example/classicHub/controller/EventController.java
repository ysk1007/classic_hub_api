package com.example.classicHub.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classicHub.entity.Event;
import com.example.classicHub.service.EventService;

@RestController
public class EventController {

	private EventService eventService;
	
	/** 생성자 **/
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}
	
	/** 공연 일정 리스트 반환 **/
	@GetMapping("/api/events")
	public List<Event> getEvent(){
		return eventService.getEvent();
	}
}
