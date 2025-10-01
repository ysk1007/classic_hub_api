package com.example.classicHub.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Video {
	
	@Id										// PK
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// auto_increment
	private int id;
	
	private int artistId;
	private String ytId;
	private String title;
	private String desc;
	private LocalDateTime uploadAt;
	private String videoUrl;
	private String thumbnailUrl;
	private int viewCount;
	private int likeCount;
	private int duration;
	private String createdAt;	
}
