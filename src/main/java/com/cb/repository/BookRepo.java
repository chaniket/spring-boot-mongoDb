package com.cb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cb.document.Book;

public interface BookRepo extends MongoRepository<Book, Integer> {
}
