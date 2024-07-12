package com.cb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cb.aop.Loggable;
import com.cb.request.LoginRequest;
import com.cb.response.LoginResponse;
import com.cb.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	LoginService loginService;

	@PostMapping("/loginUser")
	@Loggable
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
		LoginResponse response = loginService.login(loginRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/refreshToken")
	@Loggable
	public ResponseEntity<Object> refreshToken(@RequestBody LoginRequest loginRequest) {
		LoginResponse response = loginService.refreshToken(loginRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	

}
