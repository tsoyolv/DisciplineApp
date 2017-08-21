package com.olts.discipline.api.service.impl;


import com.olts.discipline.Constants;
import com.olts.discipline.api.repository.RoleRepository;
import com.olts.discipline.api.repository.UserRepository;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.model.Activity;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.Task;
import com.olts.discipline.model.User;
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
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
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
        return userRepository.findOne(userId).getHabits().stream().
                filter(h -> !h.isCompleted()).collect(Collectors.toList());
    }

    @Override // todo not ended
    public void recalculateScores(long userId, Activity activity) {
        User user = userRepository.findOne(userId);
        if (activity instanceof Habit) {
            recalculateHabit(user, activity);
        } else if (activity instanceof Task) {
            recalculateTask(user, activity);
        } else {
            recalculateChallenge(user, activity);
        }
        userRepository.save(user);
    }

    private void recalculateChallenge(User user, Activity activity) {

    }

    private void recalculateTask(User user, Activity activity) {

    }

    private void recalculateHabit(User user, Activity activity) {
        Habit habit = (Habit) activity;
        if (habit.getCompletedCount() >= Constants.HABIT_BORDER_COUNT) {
            increaseUserLevel(user);
            int newScore = habit.getDifficulty() + user.getHabitScore();
        }
    }

    private void increaseUserLevel(User user) {

        user.setLevel(user.getLevel() + 1);
    }
}
