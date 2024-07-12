package com.cb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cb.document.UserAuditEntity;

public interface UserAuditRepository extends MongoRepository<UserAuditEntity, Long> {

	Optional<UserAuditEntity> findByUsername(String username);
}
