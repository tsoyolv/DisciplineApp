package com.olts.discipline;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * OLTS on 20.05.2017.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    @Resource
    private UserService userService;

    @Resource
    private HabitRepository habitRepository;

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public void run(String... strings) throws Exception {
        User defaultUser = createDefaultUser("olts", "1");
        User defaultUser2 = createDefaultUser("user2", "2");
        authorizeUsers(defaultUser, defaultUser2);

        Habit defaultHabit = new Habit();
        defaultHabit.setName("Get up at 7 o'clock");
        defaultHabit.setDifficulty(5);
        defaultHabit.setDescription("Faster");
        defaultHabit.setHabitUser(defaultUser);
        defaultHabit.setId(1);

        habitRepository.save(defaultHabit);

        Habit defaultHabit2 = new Habit();
        defaultHabit2.setName("English 1 hour");
        defaultHabit2.setDifficulty(5);
        defaultHabit2.setDescription("Smarter");
        defaultHabit2.setHabitUser(defaultUser);
        defaultHabit2.setId(12);
        habitRepository.save(defaultHabit2);

        Habit habit = new Habit();
        habit.setName("Workout");
        habit.setDifficulty(5);
        habit.setDescription("Stronger");
        habit.setHabitUser(defaultUser2);
        habit.setId(2);

        habitRepository.save(habit);
        SecurityContextHolder.clearContext();
    }

    private User createDefaultUser(String userName, String password) {
        User defaultUser = new User();
        defaultUser.setCreatedWhen(new Date());
        defaultUser.setFirstName("Oleg");
        defaultUser.setSecondName("Tsoi");
        defaultUser.setLastName("Viacheslavovich");
        defaultUser.setUsername(userName);
        defaultUser.setPassword(password);
        defaultUser.setPasswordConfirm(password);
        defaultUser.setEmail("tsoyolv@gmail.com");
        defaultUser.setScore(9999);
        defaultUser.setHabitScore(9999);
        defaultUser.setTaskScore(9999);
        defaultUser.setLevel(99);
        defaultUser.setIsHidden(false);
        defaultUser.setLevelPercentage(85);
        defaultUser.setRank(1);
        defaultUser.setProgressPerDay(33);
        String dateInString = "8/12/1993";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            defaultUser.setBirthDate(formatter.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userService.create(defaultUser);
        return defaultUser;
    }

    private void authorizeUsers(User... users) {
        Arrays.asList(users).forEach(this::authorizeUser);
    }

    private void authorizeUser(User defaultUser) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(defaultUser.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, defaultUser.getPasswordConfirm(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}