package com.olts.discipline.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olts.discipline.api.dao.HabitDao;
import com.olts.discipline.api.dao.UserDao;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * OLTS on 23.04.2017.
 */
@Controller
@EnableTransactionManagement
@Deprecated /**
 @deprecated it used only for getting Entities
 */
public class HelloController {

    @Resource(name="habitDao")
    private HabitDao habitDao;

    @Resource(name="userDao")
    private UserDao userDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printWelcome() {
        return "index";
    }

    @RequestMapping(value = "/habit", method = RequestMethod.GET)
    @ResponseBody
    public String getHabit() {
        return habitDao.get(1).toString();
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    @Transactional(transactionManager = "hibernateTransactionManager", propagation = Propagation.REQUIRED)
    public String getUser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        final User user = userDao.get(1);
        final Set<Habit> habits = user.getHabits();
        List<String> habitJsons = new ArrayList<>(habits.size());
        habits.forEach((h) -> {
            try {
                habitJsons.add(mapper.writeValueAsString(h));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return Arrays.toString(habitJsons.toArray());
    }
}