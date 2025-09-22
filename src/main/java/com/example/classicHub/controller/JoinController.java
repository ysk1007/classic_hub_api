package com.example.classicHub.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classicHub.dto.JoinDto;
import com.example.classicHub.service.UserService;

@RestController
public class JoinController {
	
	private final UserService userService;
	
	/** 생성자 주입 **/
	public JoinController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/join")
	public String join(JoinDto joinDto) {
		
		userService.join(joinDto);
		
		return "ok";
	}
}
