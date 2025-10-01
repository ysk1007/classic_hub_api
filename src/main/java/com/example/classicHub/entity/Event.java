package com.example.classicHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Event {

	@Id														// PK
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// auto_increment
	private int id;
	
	private int artistId;
	private String title;
	private String startDt;
	private String endDt;
	private String hall;
	private String source;
	private String ticketUrl;
	private String createdAt;
}
