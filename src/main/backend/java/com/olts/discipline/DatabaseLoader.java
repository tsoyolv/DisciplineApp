package com.olts.discipline;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.repository.UserRepository;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * OLTS on 20.05.2017.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository repository;

    @Resource
    private HabitRepository habitRepository;

    @Autowired
    public DatabaseLoader(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        User defaultUser = new User();
        defaultUser.setId(1);
        defaultUser.setCreatedWhen(new Date());
        defaultUser.setFirstName("Ivan");
        defaultUser.setSecondName("Ivanov");
        defaultUser.setLastName("Ivanovich");

        this.repository.save(defaultUser);

        Habit defaultHabit = new Habit();
        defaultHabit.setName("Get up at 7 o'clock");
        defaultHabit.setDifficulty(5);
        defaultHabit.setDescription("Faster");
        defaultHabit.setHabitUser(defaultUser);
        defaultHabit.setId(1);

        habitRepository.save(defaultHabit);

        Habit habit = new Habit();
        habit.setName("Workout");
        habit.setDifficulty(5);
        habit.setDescription("Stronger");
        habit.setHabitUser(defaultUser);
        habit.setId(2);

        habitRepository.save(habit);
    }
}