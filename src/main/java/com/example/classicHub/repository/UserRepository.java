package com.example.classicHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.classicHub.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{	// 사용할 entity, pk 데이터 타입

	Boolean existsByEmail(String email);								// 해당 이메일로 가입한 USER가 있는지 확인
}
