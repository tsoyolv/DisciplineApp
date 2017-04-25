package com.olts.discipline.controller;

import com.olts.discipline.api.dao.HabitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * OLTS on 23.04.2017.
 */
@Controller
public class HelloController {

    @Autowired
    @Qualifier("habitDao")
    private HabitDao habitDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printWelcome() {
        return "index";
    }

    @RequestMapping(value = "/da", method = RequestMethod.GET)
    @ResponseBody
    public String ss() {
        return habitDao.get(1).toString();
    }
}
