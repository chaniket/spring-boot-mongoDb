package com.cb.controller;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cb.aop.Loggable;
import com.cb.document.UserEntity;
import com.cb.request.PageRequest;
import com.cb.request.UserRequest;
import com.cb.response.UserResponse;
import com.cb.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	private UserService userService;

	@Value("${spring.jpa.hibernate.ddl-auto:create}")
	String ormDDLAuto;
	
	@Autowired
	private UserController(UserService service) {
		super();
		this.userService = service;
	}

	public UserController() {
		
	}

	@PostConstruct
	public void init() {
		loadAllData();
	}

	@Autowired
	public void applicationContext(ApplicationContext applicationContext) {
		Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
	}

	@Loggable
	public void loadAllData() {

		if(ormDDLAuto!=null && !ormDDLAuto.equals("create")) {
			return ;
		}
		
		String json = "{ \"username\": \"system@gmail.com\", \"password\": \"password\", \"firstName\": \"Sharada\", \"lastName\": \"Chavan\", \"email\": \"system@gmail.com\", \"dob\": \"1996-05-13\", \"mobileNumber\": \"9503764321\", \"age\": 28, \"roleRequest\": { \"roleName\": \"SYSTEM_ADMIN\" }, \"deptRequest\": { \"deptName\": \"IT\" }, \"authoritiesRequest\": { \"authority\": \"SYSTEM_ADMIN\", \"username\": \"${username}\" } }";

		String adminJson = "{ \"username\": \"aniket@gmail.com\", \"password\": \"password\", \"firstName\": \"Sharada ACP\", \"lastName\": \"Chavan\", \"email\": \"aniket@gmail.com\", \"dob\": \"2001-06-26\", \"mobileNumber\": \"7768061024\", \"age\": 24, \"roleRequest\": { \"roleName\": \"ADMIN\" }, \"deptRequest\": { \"deptName\": \"IT\" }, \"authoritiesRequest\": { \"authority\": \"ADMIN\", \"username\": \"${username}\" } }";

		String userJson = "{ \"username\": \"aniket1@gmail.com\", \"password\": \"password\", \"firstName\": \"Aniket\", \"lastName\": \"Chavan\", \"email\": \"aniket1@gmail.com\", \"dob\": \"1996-05-13\", \"mobileNumber\": \"9503764321\", \"age\": 28, \"roleRequest\": { \"roleName\": \"USER\" }, \"deptRequest\": { \"deptName\": \"IT\" }, \"authoritiesRequest\": { \"authority\": \"USER\", \"username\": \"${username}\" } }";

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			UserRequest userRequest = objectMapper.readValue(json, UserRequest.class);
			userService.createUser(userRequest);

			userService.createUser(objectMapper.readValue(adminJson, UserRequest.class));
			userService.createUser(objectMapper.readValue(userJson, UserRequest.class));

			// Access other fields similarly
		} catch (Exception e) {
			e.printStackTrace();
		}

		PageRequest request = new PageRequest(1, 10, Sort.by(Direction.ASC, "firstName"));
		// System.out.println(userService.getAllUsers(request));
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleResourceNotFoundException(NullPointerException e) {
		return new ResponseEntity<>("Resource not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@GetMapping("/getUser/{userId}")
	@Loggable
	@PreAuthorize(value = "hasRole('ADMIN')")
	public ResponseEntity<Object> getUserById(@PathVariable("userId") String userId) {
		log.info("userId " + userId);
		// userService.asyncMethod();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder
				.getContext().getAuthentication();
		UserDetails customUserDetailService = (UserDetails) authentication.getPrincipal();// customUserDetailService.getUserEntity().getAddress();
		UserRequest request = UserRequest.builder().userId(userId).build();
		UserResponse response = userService.getUserById(request);

		if ("".equals("abc")) {
			throw new RuntimeException("thrown exception...");
		}

		return new ResponseEntity<>((response == null ? "No Data" : response), HttpStatus.OK);
	}

	@GetMapping(path = "/getAllUsers",consumes = "application/json",produces = "application/json")
	@Loggable
	@PreAuthorize(value = "hasRole('USER')")
	public ResponseEntity<Object> getAllUsers(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size) {
		// log.info("userId "+username);
		PageRequest request = new PageRequest(page.intValue(), size.intValue(), Sort.by(Direction.ASC, "firstName"));
		Page<UserEntity> response = userService.getAllUsers(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getByName/{name}")
	@Loggable
	public ResponseEntity<Object> getByName(@PathVariable("name") String name) {
		UserRequest userRequest = UserRequest.builder().firstName(name).build();
		UserResponse response = userService.getByName(userRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/createUser")
	@Loggable
	public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) {
		// UserRequest userRequest = UserRequest.builder().firstName(name).build();
		UserResponse response = userService.createUser(userRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
