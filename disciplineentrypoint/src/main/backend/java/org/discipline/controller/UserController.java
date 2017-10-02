package org.discipline.controller;

import com.olts.discipline.api.service.SecurityService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.api.validator.UserValidator;
import com.olts.discipline.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * OLTS on 13.05.2017.
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private SecurityService securityService;

    @Resource
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        // todo check for logged user
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.create(userForm);

        /* security use passwordConfirm because password already encoded */
        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        // todo check for logged user
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        model.addAttribute("userForm", new User());
        return "login";
    }


    // doesn't work
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("userForm") User userForm) {
        User findByUsername = userService.getByUsername(userForm.getUsername());
        if (!findByUsername.equals(userForm)) {
            return "login";
        } else {
            securityService.autologin(userForm.getUsername(), userForm.getPassword());
            return "redirect:/";
        }
    }

    @RequestMapping(value = {"/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }
}