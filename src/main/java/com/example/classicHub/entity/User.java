package com.example.classicHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
	
	@Id														// PK
	@GeneratedValue(strategy = GenerationType.IDENTITY)		// auto_increment
	private int id;
	
	private String email;
	private String password;
	private String name;
	private String role;
	
}
