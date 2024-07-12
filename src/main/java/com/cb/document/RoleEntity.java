package com.cb.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Data
@Document(collation = "role")
public class RoleEntity {

	// @Id
	@MongoId
	String id;

	String name;

}
