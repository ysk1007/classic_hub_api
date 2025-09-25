package com.example.classicHub.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	private SecretKey secretKey;
	
	/** 생성자 **/
	public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

		byte[] byteSecretKey = Decoders.BASE64.decode(secret);
		secretKey = Keys.hmacShaKeyFor(byteSecretKey);
    }
	
	// 토큰을 받고 검증하는 3가지 메서드
	// 토큰을 받고 setSigningKey로 내 프로그램의 secretKey와 동일한지 확인
	// parseClaimsJws로 내부 Claims 확인하고 get으로 데이터를 가져옴
	
	// 토큰을 전달 받아서 email 가져오기
	public String getEmail(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("email", String.class);
    }

	// 토큰을 전달 받아서 role 가져오기
    public String getRole(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    // 토큰을 전달 받아서 만료 여부 확인하기
    public Boolean isExpired(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    // 토큰 발급
    public String createJwt(String email, String role, Long expiredMs) {

    	// claims 저장
		Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims) 													// 토큰에 데이터 저장
                .setIssuedAt(new Date(System.currentTimeMillis()))					// 발행 시간 저장
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))	// 발행 시간 + 유효 기간 = 만료 시간을 저장
                .signWith(secretKey, SignatureAlgorithm.HS256)						// 암호키와 HS256 암호화 알고리즘으로 암호키까지 저장
                .compact();
    }
	
}
