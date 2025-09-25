package com.example.classicHub.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.classicHub.dto.CustomUserDetails;
import com.example.classicHub.entity.User;
import com.example.classicHub.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	/** 생성자 **/
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository; 
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email);	// 이메일로 해당 이메일을 사용하는 유저를 검색
		
		if(user != null) {								// USER가 있으면
			return new CustomUserDetails(user);			// CustomUserDetails를 반환
		}
		
		return null;
	}
}
