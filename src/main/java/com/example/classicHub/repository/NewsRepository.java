package com.example.classicHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.classicHub.entity.News;

public interface NewsRepository extends JpaRepository<News, Integer>{
	
	Boolean existsByNewsUrl(String newsUrl);
}
