package com.olts.discipline.api.service;


import com.olts.discipline.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
