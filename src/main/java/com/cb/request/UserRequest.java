package com.cb.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

	String userId;
	// @NonNull
	String username;
	// @NonNull
	String password;
	// @NonNull
	String firstName;
	// @NonNull
	String lastName;
	// @NonNull
	String email;
	// @NonNull
	String mobileNumber;
	// @NonNull
	Date dob;
	// @NonNull
	Long age;

	// @NonNull
	RoleRequest roleRequest;
	
	DeptRequest deptRequest;

	// @NonNull
	AuthoritiesRequest authoritiesRequest;
}
