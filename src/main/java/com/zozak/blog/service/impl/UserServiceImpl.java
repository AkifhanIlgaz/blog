package com.zozak.blog.service.impl;

import com.zozak.blog.domain.entity.User;
import com.zozak.blog.repository.UserRepository;
import com.zozak.blog.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID id) {
        return userRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + id)
            );
    }
}
