package com.olts.discipline.controller;

import com.olts.discipline.api.dao.UserDao;
import com.olts.discipline.model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * o.tsoy
 * 26.04.2017
 */
@Controller
@Deprecated
/** @deprecated move it to reactjs */
public class LoginController {
    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    @Resource(name = "userDao")
    private UserDao userDao;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView displayLogin() {
        ModelAndView model = new ModelAndView("login/login");
        User user = new User();
        model.addObject("user", user);
        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView executeLogin(@ModelAttribute("user") User user) {
        ModelAndView model;
        final User retrievedUser = userDao.get(user.getLogin());
        LOGGER.debug(retrievedUser.toString());
        if (retrievedUser != null && retrievedUser.getPassword().equals(user.getPassword())) {
            System.out.println("User Login Successful");
            //todo request.setAttribute("loggedInUser", loginBean.getUsername());
            model = new ModelAndView("index");
        } else {
            model = new ModelAndView("login/login");
            model.addObject("user", user);
            //todo request.setAttribute("message", "Invalid credentials!!");
        }
        return model;
    }

}
