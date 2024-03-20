package com.Team12.HADBackEnd.repository;

import java.util.Optional;

import com.Team12.HADBackEnd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);
  User findByEmail(String email);
  User findByResetToken(String resetToken);

  Boolean existsByEmail(String email);
}
