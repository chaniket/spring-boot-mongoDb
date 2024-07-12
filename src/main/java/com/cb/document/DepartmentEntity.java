package com.cb.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Data
@Document(collation = "department")
public class DepartmentEntity {

	//@Id
	@MongoId
	String id;

	String name;

}
