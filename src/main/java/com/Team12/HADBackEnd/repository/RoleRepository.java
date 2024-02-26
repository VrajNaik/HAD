package com.Team12.HADBackEnd.repository;

import java.util.Optional;

import com.Team12.HADBackEnd.models.ERole;
import com.Team12.HADBackEnd.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
