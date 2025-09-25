package com.example.classicHub.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.classicHub.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;			// 검증 매니저
	private final JWTUtil jwtutil;
	
	/** 생성자 **/
	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtutil) {
		this.authenticationManager = authenticationManager;
		this.jwtutil = jwtutil;
	}	
	
	// 검증 과정
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		// 클라이언트 요청에서 email, password 추출
		String email = request.getParameter("email");	// 기존의 username을 안 쓰고 param 값을 email로 할거임
		String password = obtainPassword(request);	
		
		// 넘길 토큰
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
		
		
		// authenticationManager 가 검증 진행
		return authenticationManager.authenticate(authToken);
	}
	
	// 로그인 성공시 실행하는 메소드 (여기서 JWT 발급)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
	
		System.out.println("com.example.classicHub.jwt.LoginFilter : 로그인 성공");
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
		
		// 이메일 가져오기
		String email = customUserDetails.getEmail();
		
		// 역할 가져오기
		Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		
		String role = auth.getAuthority();
		
		// 토큰 만들기
		String token = jwtutil.createJwt(email, role, 60*60*10L);
		
		// HTTP 인증 방식은 RFC 7235 정의에 따라 아래 인증 헤더 형태를 가져야 함.
		// Authorization : Bearer {인증토큰}
		response.addHeader("Authorization", "Bearer " + token);
	}
	
	// 로그인 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		System.out.println("com.example.classicHub.jwt.LoginFilter : 로그인 실패");
		
		// 로그인 실패시 401 응답
		response.setStatus(401);
	}
}
