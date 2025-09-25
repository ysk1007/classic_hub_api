package com.example.classicHub.controller;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@GetMapping("/")
	public String mainP() {
		
		// 현재 세션 사용자 이메일
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// 현재 세션 사용자 role		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iter = authorities.iterator();
		GrantedAuthority auth = iter.next();
		String role = auth.getAuthority();
		
		return "main page " + email + " " + role;
	}
	
	@GetMapping("/admin")
	public String adminP() {
		return "admin page";
	}
}
