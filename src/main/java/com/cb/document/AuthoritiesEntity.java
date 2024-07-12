package com.cb.document;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "authorities")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class AuthoritiesEntity {

	String id;
	@DBRef
	LoginEntity loginEntity;

	String username;

	String authority;

}
