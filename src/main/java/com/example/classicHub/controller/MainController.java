package com.example.classicHub.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classicHub.entity.User;
import com.example.classicHub.service.YoutubeVideoService;

@RestController
public class MainController {

	@Autowired YoutubeVideoService yvs;
	
	@GetMapping("/")
	public User mainP() {
		
		// 현재 세션 사용자 이메일
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// 현재 세션 사용자 role		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iter = authorities.iterator();
		GrantedAuthority auth = iter.next();
		String role = auth.getAuthority();
		
		User user = new User();
		user.setEmail(email);
		user.setRole(role);
		
		try {
			List<String> ytIdList = yvs.searchVideoIds("임윤찬", 5);
			yvs.fetchVideos(ytIdList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
	
	@GetMapping("/admin")
	public String adminP() {
		return "admin page";
	}
}
