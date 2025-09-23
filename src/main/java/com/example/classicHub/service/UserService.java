package com.example.classicHub.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.classicHub.dto.JoinDto;
import com.example.classicHub.entity.User;
import com.example.classicHub.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/** 생성자 **/
	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	/** 회원가입 프로세스 **/
	public boolean join(JoinDto joinDto) {
		
		String email = joinDto.getEmail();
		String password = joinDto.getPassword();
		String name = joinDto.getName();
		
		boolean isExist = userRepository.existsByEmail(email);		// 이메일로 회원가입 되어있는지 확인
		
		if(isExist) {	// 사용중
			return false;
		}
		
		User user = new User();
		user.setEmail(email);
		user.setPassword(bCryptPasswordEncoder.encode(password));	// 패스워드는 꼭 암호화
		user.setName(name);
		user.setRole("ROLE_ADMIN");									// 임시 역할
		
		userRepository.save(user);									// 실제 DB 저장
		
		return true;
	}
}
