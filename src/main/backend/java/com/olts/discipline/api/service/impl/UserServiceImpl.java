package com.olts.discipline.api.service.impl;


import com.olts.discipline.api.repository.RoleRepository;
import com.olts.discipline.api.repository.UserRepository;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

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

    @Override
    public User getCurrent() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername());
    }

    @Override
    public Collection<Habit> getUserHabits(long userId) {
        return userRepository.findOne(userId).getHabits();
    }

    @Override
    public Collection<Habit> getNotCompletedUserHabits(long userId) {
        return userRepository.findOne(userId).getHabits().stream().
                filter(h -> !h.isCompleted()).collect(Collectors.toList());
    }

    @Override
    public Collection<Habit> getCompletedUserHabits(long userId) {
        return userRepository.findOne(userId).getHabits().stream().
                filter(Habit::isCompleted).collect(Collectors.toList());
    }
}