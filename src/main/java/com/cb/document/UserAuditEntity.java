package com.cb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuditEntity {

	@Id
	String id;

	String idAddress;
	String username;
	Integer totalLoginAttempts;
	Integer totalFailedAttempts;
	Integer successLoginAttempts;
	Boolean isLocked;
}
