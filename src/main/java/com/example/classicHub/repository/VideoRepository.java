package com.example.classicHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.classicHub.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {

}
