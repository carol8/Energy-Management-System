package com.carol8.security_microservice.repository;

import com.carol8.security_microservice.entity.User;
import com.carol8.security_microservice.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findUsersByRoleIs(UserRole userRole);
}
