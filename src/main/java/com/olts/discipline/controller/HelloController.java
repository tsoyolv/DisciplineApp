package com.olts.discipline.controller;

import com.olts.discipline.api.dao.HabitDao;
import com.olts.discipline.api.dao.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * OLTS on 23.04.2017.
 */
@Controller
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
    public String getUser() {
        return Arrays.toString(userDao.get().toArray());
    }
}