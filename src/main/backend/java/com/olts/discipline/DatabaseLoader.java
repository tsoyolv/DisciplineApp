package com.olts.discipline;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.service.GroupService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Group;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        User defaultUser = createDefaultUser("olts", "1", "Oleg", "Tsoi", false);
        User defaultUser2 = createDefaultUser("user2", "2", "Hidden", "User", true);
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

        testGroup(defaultUser);

        SecurityContextHolder.clearContext();
    }

    protected void testGroup(User defaultUser) {
        createGroups(defaultUser);
        List<User> createdGroups = userService.getByGroup(1L, 0, 5).getContent();
        if (createdGroups.isEmpty()) {
            throw new RuntimeException("Failed");
        }
    }

    private void createGroups(User defaultUser) {
        createGroup(defaultUser);
    }

    @Resource
    private GroupService groupService;

    private void createGroup(User creator) {
        User u1 = new User(11, "u1", "u1first", 3, 3);
        User u2 = new User(12, "u2", "u2first", 4, 4);
        User u3 = new User(13, "u3", "u3first", 5, 5);
        userService.update(u1);
        userService.update(u2);
        userService.update(u3);
        List<User> gUsers = new ArrayList<User>() {{
            add(u1);
            add(u2);
            add(u3);
        }};

        Group group = new Group(1, "friends");
        group.setCreatedBy(creator);
        group.setUsers(gUsers);

        groupService.create(group);
    }

    private User createDefaultUser(String userName, String password, String firstName, String secondName, boolean hidden) {
        User defaultUser = new User();
        defaultUser.setCreatedWhen(new Date());
        defaultUser.setFirstName(firstName);
        defaultUser.setSecondName(secondName);
        defaultUser.setLastName("Viacheslavovich");
        defaultUser.setUsername(userName);
        defaultUser.setPassword(password);
        defaultUser.setPasswordConfirm(password);
        defaultUser.setEmail("tsoyolv@gmail.com");
        defaultUser.setScore(9999);
        defaultUser.setHabitScore(9999);
        defaultUser.setTaskScore(9999);
        defaultUser.setLevel(99);
        defaultUser.setHidden(hidden);
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