package com.disciplineapp.api.service.impl;


import com.disciplineapp.api.repository.RoleRepository;
import com.disciplineapp.api.repository.UserRepository;
import com.disciplineapp.api.service.UserService;
import com.disciplineapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void create(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public User get(long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * get current logged user
     * @return null if there is no logged users
     * */
    @Override
    public User getCurrent() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            return null;
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> getByGroup(Long groupId, int page, int size) {
        return userRepository.findByUserGroup(groupId, new PageRequest(page, size, Sort.Direction.DESC, "rank"));
    }
}