package com.uber.rocket.service;

import com.uber.rocket.entity.user.Role;
import com.uber.rocket.entity.user.RoleType;
import com.uber.rocket.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleByUserRole(RoleType roleType) {
        Optional<Role> role = roleRepository.findByRole(roleType.name());
        if (role.isEmpty())
            throw new RuntimeException("This role doesn't exist");
        return role.get();
    }
}
