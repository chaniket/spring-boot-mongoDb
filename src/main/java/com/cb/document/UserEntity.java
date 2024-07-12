package com.cb.document;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "user_details")
public class UserEntity {

	@Id
	String id;
	String firstName;
	String lastName;
	String email;
	String mobileNumber;

	Date dob;
	Long age;

	//@DBRef
	DepartmentEntity deptId;

	//@DBRef
	RoleEntity roleId;

	@JsonManagedReference
	List<UserAddressEntity> address;


}
