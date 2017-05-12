package com.olts.discipline.controller;

import com.olts.discipline.api.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * OLTS on 12.05.2017.
 */
@Controller
public class UserSimpleController {

    @Resource
    private UserRepository repository;

    @RequestMapping("simple/user")
    @ResponseBody
    public String allUsers() {
        final String[] s = {""};
        repository.findAll().forEach(e -> s[0] += e + "\n");
        return s[0];
    }
}
