package com.uber.rocket.repository;

import com.uber.rocket.dto.UserDataDTO;
import com.uber.rocket.entity.user.Role;
import com.uber.rocket.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<UserDataDTO> searchAllByFirstNameStartingWithOrLastNameStartingWithAndRolesContains(String firstName, String lastName, Role role, PageRequest pageRequest);
}
