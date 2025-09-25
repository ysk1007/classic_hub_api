package com.example.classicHub.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.classicHub.dto.CustomUserDetails;
import com.example.classicHub.entity.User;
import com.example.classicHub.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter{

	private final JWTUtil jwtutil;
	
	/** 생성자 **/
	public JWTFilter(JWTUtil jwtutil) {
		this.jwtutil = jwtutil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// 1) 토큰 검증
		
		// request에서 Authorization 헤더를 찾음
		String authorization = request.getHeader("Authorization");
		
		// Authorization 헤더 검증
		if(authorization == null || !authorization.startsWith("Bearer ")) {	// 토큰이 없거나, 접두사 시작이 Bearer이 아님
			
			System.out.println("com.example.classicHub.jwt.JWTFilter : 토큰 검증 실패");
			
			filterChain.doFilter(request, response);
			
			return;			
		}
		
		// 2) 토큰 소멸 시간 검증
		String token = authorization.split(" ")[1];	// 접두사 부분 제거하고 순수 토큰만 가져옴
		
		if(jwtutil.isExpired(token)) {
			System.out.println("com.example.classicHub.jwt.JWTFilter : 토큰 만료됨");
			
			filterChain.doFilter(request, response);
			
			return;	
		}
		
		// 토큰 검증 성공
		System.out.println("com.example.classicHub.jwt.JWTFilter : 토큰 검증 성공");
		String email = jwtutil.getEmail(token);
		String role = jwtutil.getRole(token);
		
		// User 엔티티를 생성하여 값 할당
		User user = new User();
		
		user.setEmail(email);
		user.setPassword("temppassword");
		user.setRole(role);
		
		// UserDetails에 회원 정보 담기
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		// 스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		
		// 세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		// 다음 필터로 request, response 넘기기
		filterChain.doFilter(request, response);
		
	}
}
