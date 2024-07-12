package com.cb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb.document.AuthoritiesEntity;
import com.cb.document.LoginEntity;
import com.cb.document.User;
import com.cb.repository.AuthoritiesRepository;
import com.cb.repository.LoginRepository;
import com.cb.repository.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepo userRepository;
	
	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	AuthoritiesRepository authoritiesRepository;
	

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginEntity user = loginRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		List<AuthoritiesEntity> authorities = authoritiesRepository.findByUsername(username);
		user.setAuthorities(authorities);
		return user;
	}

}