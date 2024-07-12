package com.cb.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

	String username;
	String password;
	String ipAddress;
}
