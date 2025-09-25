package com.example.classicHub.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.classicHub.jwt.JWTFilter;
import com.example.classicHub.jwt.JWTUtil;
import com.example.classicHub.jwt.LoginFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTUtil jwtutil;
	
	/** 생성자 주입 **/
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtutil) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtutil = jwtutil;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {		// Spring Security 인증/인가는 항상 비밀번호를 HASH로 암호화하여 진행한다.
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.cors((cors)-> cors.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);

									configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                return configuration;
			}
		}));
		
		// csrf disable
		http.csrf((auth) -> auth.disable());
		
		// Form 로그인 방식 disable
		http.formLogin((auth) -> auth.disable());
		
		// http basic 인증 방식 disable
		http.httpBasic((auth) -> auth.disable());
		
		// 경로별 인가 작업
		http.authorizeHttpRequests((auth) -> auth	
					.requestMatchers("/login", "/","/join").permitAll()			// 이 경로에 대해서는 모든 권한을 허가한다.
					.requestMatchers("/admin").hasRole("ADMIN")					// 이 경로에 대해서는 해당 권한이 있는 사용자만 접근을 허가한다.
					.anyRequest().authenticated()								// 그 외 경로에는 로그인된 사용자만 권한을 허가한다.
				);
		
		// JWTFilter 등록
		http.addFilterBefore(new JWTFilter(jwtutil), LoginFilter.class);
		
		// 커스텀 필터 추가
		http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtutil), UsernamePasswordAuthenticationFilter.class);
		
		// 세션 설정
		http.sessionManagement((session) -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)	// JWT를 통한 인증/인가를 위해서 세션을 STATELESS 상태로 설정.
				);
		
		
		return http.build();
	}
}
