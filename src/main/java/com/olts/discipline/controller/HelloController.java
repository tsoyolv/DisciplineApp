package com.olts.discipline.controller;

import com.olts.discipline.api.dao.HabitDao;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printWelcome() {
        return "index";
    }

    @RequestMapping(value = "/da", method = RequestMethod.GET)
    @ResponseBody
    public String ss() {
        return Arrays.toString(habitDao.get().toArray());
    }
}
