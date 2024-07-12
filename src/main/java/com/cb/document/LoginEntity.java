package com.cb.document;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@Document(collection = "login_tbl")
public class LoginEntity implements UserDetails {

	private static final long serialVersionUID = 1468296879147987080L;

	String id;
	String username;
	String password;

	Boolean enabled;

	Long loginAttempt;

	Long failedAttempt;

	@DBRef
	UserEntity userEntity;

	@DBRef
	List<AuthoritiesEntity> authorities;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {

		return this.enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities.stream().map(rs -> new SimpleGrantedAuthority(rs.getAuthority()))
				.collect(Collectors.toList());
	}

}
