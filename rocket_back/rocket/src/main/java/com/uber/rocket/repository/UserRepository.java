package com.uber.rocket.repository;

import com.uber.rocket.dto.UserDataDTO;
import com.uber.rocket.entity.user.Role;
import com.uber.rocket.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT user FROM User user LEFT JOIN user.roles role WHERE role.role = :role AND (user.firstName like :filter% OR user.lastName like :filter%)")
    Page<UserDataDTO> searchAllFirstNameStartingWithOrLastNameStartingWith(@Param("role") String role, @Param("filter") String filter, PageRequest pageRequest);

    List<User> findAllByRolesContaining(Role roles);
}
