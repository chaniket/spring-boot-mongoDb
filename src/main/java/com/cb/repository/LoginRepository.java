package com.cb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cb.document.LoginEntity;

public interface LoginRepository extends MongoRepository<LoginEntity, Long>{

	boolean existsByUsername(String username);

	Optional<LoginEntity> findByUsername(String username);

}
