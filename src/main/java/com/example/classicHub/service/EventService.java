package com.example.classicHub.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.classicHub.entity.Event;
import com.example.classicHub.repository.EventRepository;

@Service
public class EventService {

	private EventRepository eventRepository;
	
	public EventService(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	/** 이벤트 **/
	public List<Event> getEvent(){
		return eventRepository.findAll();
	}
	
	/** 티켓팅 사이트 파이썬 크롤러로 읽기 **/
	public void eventUpdate() {
		try {
            ProcessBuilder pb = new ProcessBuilder(
                    "python", 
                    "D:/A_MyTask/crawler/venv/Scripts/ticket.py"
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Python 로그 출력 읽기
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[Python] " + line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Python 크롤러 실행 완료 (ExitCode: " + exitCode + ")");
        } catch (Exception e) {
            System.err.println("Python 크롤러 실행 실패: " + e.getMessage());
        }
	}
}
