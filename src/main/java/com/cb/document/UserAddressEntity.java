package com.cb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Document(collection = "user_address")
public class UserAddressEntity {

	@Id
	String id;
	String address;
	String pincode;
	String street;
	
	@JsonBackReference
	UserEntity userEntity;
}
