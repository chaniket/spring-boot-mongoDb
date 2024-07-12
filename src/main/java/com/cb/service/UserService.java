package com.cb.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb.aop.Loggable;
import com.cb.document.AuthoritiesEntity;
import com.cb.document.DepartmentEntity;
import com.cb.document.LoginEntity;
import com.cb.document.RoleEntity;
import com.cb.document.UserEntity;
import com.cb.repository.AuthoritiesRepository;
import com.cb.repository.LoginRepository;
import com.cb.repository.UserRepository;
import com.cb.request.UserRequest;
import com.cb.response.UserResponse;

@Service
public class UserService {

	UserRepository userRepository;

	LoginRepository loginRepository;

	AuthoritiesRepository authoritiesRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private UserService(UserRepository userRepository, LoginRepository loginRepository,
			AuthoritiesRepository authoritiesRepository) {
		super();
		this.userRepository = userRepository;
		this.loginRepository = loginRepository;
		this.authoritiesRepository = authoritiesRepository;
	}

	public UserService() {
		// TODO Auto-generated constructor stub
	}
	
	@Transactional(readOnly = true)
	public UserResponse getUserById(UserRequest request) {
		Optional<UserEntity> userDetails = userRepository.findById(request.getUserId());
		UserResponse response = new UserResponse();
		if (userDetails.isPresent())
			BeanUtils.copyProperties(userDetails.orElse(null), response);

		return response;
	}

	@Transactional(readOnly = true)
	public Page<UserEntity> getAllUsers(PageRequest request) {

		return userRepository.findAll(request);
	}

	@Transactional
	public UserResponse createUser(UserRequest loginRequest) {
		UserResponse response = new UserResponse();
		UserEntity userEntity = new UserEntity();
		LoginEntity loginEntity = new LoginEntity();
		if(loginRepository.existsByUsername(loginRequest.getUsername())) {
			//throw new UserNotFoundException("User details already present!, Pls use someother details...");
			return response;
		}
		AuthoritiesEntity authoritiesEntity = null;

		BeanUtils.copyProperties(loginRequest, userEntity);
		
		DepartmentEntity deptId = new DepartmentEntity();
		deptId.setName(loginRequest.getDeptRequest().getDeptName());
		RoleEntity roleId = new RoleEntity();
		roleId.setName(loginRequest.getRoleRequest().getRoleName());
		BeanUtils.copyProperties(loginRequest.getRoleRequest(), roleId);
		BeanUtils.copyProperties(loginRequest.getDeptRequest(), deptId);
		userEntity.setRoleId(roleId);
		userEntity.setDeptId(deptId);
		userRepository.save(userEntity);

		BeanUtils.copyProperties(loginRequest, loginEntity);
		loginEntity.setPassword(passwordEncoder.encode(loginEntity.getPassword()));
		loginEntity.setEnabled(Boolean.TRUE);
		loginEntity.setUserEntity(userEntity);
		loginRepository.save(loginEntity);
		
		authoritiesEntity = AuthoritiesEntity.builder().loginEntity(loginEntity).username(loginRequest.getUsername())
				.authority("ROLE_"+loginRequest.getAuthoritiesRequest().getAuthority()).build();
		
		authoritiesRepository.save(authoritiesEntity);
		loginEntity.setAuthorities(Arrays.asList(authoritiesEntity));
		loginRepository.save(loginEntity);
		BeanUtils.copyProperties(userEntity, response);

		return response;
	}
	
	@Async
	@Loggable
	public CompletableFuture<String> asyncMethod() {
		System.out.println("UserService.asyncMethod()");
		return null;
    }

	public UserResponse getByName(UserRequest userRequest) {
		Optional<UserEntity> userDetails = userRepository.findByFirstName(userRequest.getFirstName());
		UserResponse response = new UserResponse();
		if (userDetails.isPresent())
			BeanUtils.copyProperties(userDetails.orElse(null), response);

		return response;
	
	}

}
