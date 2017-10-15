package com.disciplineapp.controller;

import com.disciplineapp.api.service.SecurityService;
import com.disciplineapp.entity.User;
import com.disciplineapp.api.service.UserService;
import com.disciplineapp.api.validator.UserValidator;
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
public class LoginController {
    @Resource
    private UserService userService;
    @Resource
    private SecurityService securityService;
    @Resource
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        if (userService.getCurrent() != null) {
            return "redirect:/";
        }
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
        if (userService.getCurrent() != null) {
            return "redirect:/";
        }
        if (error != null)
            model.addAttribute("error", "Your email and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        model.addAttribute("userForm", new User());
        return "login";
    }
}