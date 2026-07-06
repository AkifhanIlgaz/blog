package com.zozak.blog.service;

import com.zozak.blog.domain.entity.User;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
