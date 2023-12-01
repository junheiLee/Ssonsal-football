package com.ssonsal.football.user.repository;

import com.ssonsal.football.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
