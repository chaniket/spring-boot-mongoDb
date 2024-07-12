package com.cb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Books")
// Class
//collection means table
//Field mean column
//row means document
public class Book {

	// Attributes
	@Id
	private String id;
	private String bookName;
	private String authorName;
}