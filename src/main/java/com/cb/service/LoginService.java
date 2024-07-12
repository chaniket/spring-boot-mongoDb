package com.cb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


import com.cb.document.LoginEntity;
import com.cb.document.UserAuditEntity;
import com.cb.repository.LoginRepository;
import com.cb.repository.UserAuditRepository;
import com.cb.repository.UserRepository;
import com.cb.request.LoginRequest;
import com.cb.request.UserRequest;
import com.cb.response.LoginResponse;
import com.cb.response.UserResponse;
import com.cb.util.JwtUtil;

@Service
public class LoginService {

	LoginRepository loginRepository;
	PasswordEncoder passwordEncoder;
	UserRepository userRepository;
	UserService userService;
	UserAuditRepository auditRepository;

	@Autowired
	JwtUtil jwtUtil;

	// @Autowired
	// private AuthenticationManager authenticationManager;

	@Autowired
	private LoginService(LoginRepository loginRepository, PasswordEncoder passwordEncoder,
			UserRepository userRepository, UserService userService, UserAuditRepository auditRepository) {
		super();
		this.loginRepository = loginRepository;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.userService = userService;
		this.auditRepository = auditRepository;
	}

	public LoginResponse login(LoginRequest loginRequest) {

//		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//        if (authentication.isAuthenticated()) {
//           // return jwtService.generateToken(authRequest.getUsername());
//        } else {
//            throw new UsernameNotFoundException("invalid user request !");
//        }
		LoginResponse loginResponse = new LoginResponse();
		LoginEntity loginEntity = loginRepository.findByUsername(loginRequest.getUsername()).orElse(null);
		if (loginEntity != null) {
			if (passwordEncoder.matches(loginRequest.getPassword(), loginEntity.getPassword())) {
				loginRequest.setPassword(null);
				UserAuditEntity auditEntity = auditRepository.findByUsername(loginEntity.getUsername()).orElse(null);
				if (auditEntity != null && auditEntity.getIsLocked() != null
						&& Boolean.TRUE.equals(auditEntity.getIsLocked())) {
					throw new UsernameNotFoundException("User Account is locked!");
				}
				UserRequest request = UserRequest.builder().userId(loginEntity.getUserEntity().getId()).build();
				UserResponse userResponse = userService.getUserById(request);
				loginResponse.setUserResponse(userResponse);
				loginResponse.setUsername(loginRequest.getUsername());
				loginResponse.setAuthToken(jwtUtil.generateToken(loginEntity));

				updateAuditEntity(auditEntity, loginRequest, "success");
				System.out.println("jwtUtil.getTokenSuject(loginResponse.getAuthToken()) \nusername -"
						+ jwtUtil.getTokenSuject(loginResponse.getAuthToken()));
				return loginResponse;
			} else {
				UserAuditEntity auditEntity = auditRepository.findByUsername(loginRequest.getUsername()).orElse(null);
				updateAuditEntity(auditEntity, loginRequest, "failed");
				throw new UsernameNotFoundException("User details are not correct!");
			}
		} else {
			UserAuditEntity auditEntity = auditRepository.findByUsername(loginRequest.getUsername()).orElse(null);
			updateAuditEntity(auditEntity, loginRequest, "failed");
			throw new UsernameNotFoundException("User details not found!");
		}
	}

	public void updateAuditEntity(UserAuditEntity auditEntity, LoginRequest loginRequest, String action) {
		if (auditEntity == null)
			auditEntity = new UserAuditEntity();

		RequestAttributes contextHolder = RequestContextHolder.currentRequestAttributes();
		System.out.println(contextHolder);
		auditEntity.setUsername(loginRequest.getUsername());
		if (action.equals("success")) {
			auditEntity.setIsLocked(false);
			auditEntity.setTotalLoginAttempts(0);
			auditEntity.setTotalFailedAttempts(0);
			auditEntity.setSuccessLoginAttempts(
					(auditEntity.getTotalLoginAttempts() == null ? 0 : auditEntity.getTotalLoginAttempts()) + 1);
		} else {
			auditEntity.setTotalFailedAttempts(
					(auditEntity.getTotalLoginAttempts() == null ? 0 : auditEntity.getTotalLoginAttempts()) + 1);
			auditEntity.setTotalLoginAttempts(
					(auditEntity.getTotalLoginAttempts() == null ? 0 : auditEntity.getTotalLoginAttempts()) + 1);
			if (auditEntity.getTotalFailedAttempts().intValue() >= 3) {
				auditEntity.setIsLocked(true);
			}
		}
		auditRepository.save(auditEntity);
	}

	public LoginResponse createUser(LoginRequest loginRequest) {

		return null;
	}

	public LoginResponse refreshToken(LoginRequest loginRequest) {

		return null;
	}

}
