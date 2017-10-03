package com.disciplineapp.configuration;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * OLTS on 22.08.2017.
 */
//@Configuration todo doesn't work. was done for inheritanced habit from activity
public class RepositoryEntityLookupConfig extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration) {
        /*configuration
                .withEntityLookup()
                .forValueRepository(HabitRepository.class, Habit::getId, HabitRepository::findOne);*/
    }
}