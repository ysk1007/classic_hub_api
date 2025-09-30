package com.example.classicHub.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.classicHub.entity.News;
import com.example.classicHub.repository.NewsRepository;

@Service
public class NewsService {

	private NewsRepository newsRepository;
	
	/** 생성자 **/
	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}
	
	/** news 가져오기 **/
	public List<News> getNews(){
		return newsRepository.findAll();
	}
	
	/** 뉴스 본문 파이썬 크롤러로 읽기 **/
	public void newsUpdate() {
		try {
            ProcessBuilder pb = new ProcessBuilder(
                    "python", 
                    "D:/A_MyTask/crawler/venv/Scripts/update_news_body.py"
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
