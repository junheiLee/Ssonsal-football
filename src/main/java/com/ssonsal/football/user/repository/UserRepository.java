package com.ssonsal.football.user.repository;

import com.ssonsal.football.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByEmail(String email);

    Optional<User> findById(Long id);

    Boolean existsByEmail(String email);

}
