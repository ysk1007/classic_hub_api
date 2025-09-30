package com.example.classicHub.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.classicHub.service.NewsSearchService;
import com.example.classicHub.service.NewsService;

@Component
public class NewsUpdateScheduler {

	private NewsService newsService;
	private NewsSearchService newsSearchService;
	
	/** 생성자 **/
	public NewsUpdateScheduler(NewsService newsService, NewsSearchService newsSearchService) {
		this.newsService = newsService;
		this.newsSearchService = newsSearchService;
	}
	
	
	@Scheduled(cron = "0 0 3 * * *")
	public void newsUpdate() {
		System.out.println("com.example.classicHub.scheduler.NewsUpdateScheduler : 뉴스 업데이트 스케줄러 실행!");
		
		newsSearchService.newsSearch();	// 네이버 api 뉴스 크롤링		
		
		newsService.newsUpdate();		// 뉴스 본문 크롤링 후, 업데이트
	}
}
