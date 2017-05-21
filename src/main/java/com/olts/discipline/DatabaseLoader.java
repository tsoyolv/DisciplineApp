package com.olts.discipline;

import com.olts.discipline.api.repository.UserRepository;
import com.olts.discipline.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * OLTS on 20.05.2017.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository repository;

    @Autowired
    public DatabaseLoader(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        User newUser = new User();
        newUser.setCreatedWhen(new Date());
        newUser.setFirstName("Oleg");
        newUser.setSecondName("Tsoi");
        newUser.setLastName("Vyacheslavovich");
        newUser.setUsername("OLTS");
        this.repository.save(newUser);
    }
}