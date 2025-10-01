package com.example.classicHub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.classicHub.entity.Video;
import com.example.classicHub.repository.VideoRepository;

@Service
public class VideoService {

	private VideoRepository videoRepository;
	
	public VideoService() {
		this.videoRepository = videoRepository;
	}
	
	public void videoSave(List<Video> videoList) {
		for(Video video : videoList) {
			
			
		}
	}
	
	
}
