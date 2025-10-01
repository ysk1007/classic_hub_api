package com.example.classicHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.classicHub.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
	
}
