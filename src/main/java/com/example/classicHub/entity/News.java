package com.example.classicHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class News {
	
	@Id														// PK
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// auto_increment
	private int id;
	
	private int artistId;
	private String publisher;
	private String title;
	private String uploadAt;
	private String newsUrl;
	private String imageUrl;
	private String summary;
	private String createdAt;
}
