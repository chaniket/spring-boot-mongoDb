package com.cb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cb.document.AuthoritiesEntity;

public interface AuthoritiesRepository extends MongoRepository<AuthoritiesEntity, Long>{

	List<AuthoritiesEntity> findByUsername(String username);
}
