package com.cb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "clients")
@Setter
@Getter
public class Client {
	@Id
	private String clientId;
	private String clientSecret;

	private String redirectUri;

	private String scope;
	private String authorizedGrantTypes;

}