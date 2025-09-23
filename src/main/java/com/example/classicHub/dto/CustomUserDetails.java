package com.example.classicHub.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.classicHub.entity.User;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails{
	
	private final User user;
	
	/** 생성자 **/
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	/** role 반환 **/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		
		collection.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		
		return collection;
	}

	/** 비밀번호 반환 **/
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/** 아이디 반환 **/
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}
