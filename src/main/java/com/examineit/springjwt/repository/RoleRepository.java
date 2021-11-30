package com.examineit.springjwt.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.examineit.springjwt.models.ERole;
import com.examineit.springjwt.models.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
