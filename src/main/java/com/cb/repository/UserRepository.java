package com.cb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cb.document.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String> {

	Optional<UserEntity> findByFirstName(String firstName);

}
