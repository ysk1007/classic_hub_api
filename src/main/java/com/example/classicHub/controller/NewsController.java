package com.example.classicHub.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classicHub.entity.News;
import com.example.classicHub.service.NewsService;

@RestController
public class NewsController {
	
	private final NewsService newsService;
	
	public NewsController(NewsService newsService) {
		this.newsService = newsService;
	}
	
	@GetMapping("/api/news")
	public List<News> news() {
		return newsService.getNews();
	}
}
