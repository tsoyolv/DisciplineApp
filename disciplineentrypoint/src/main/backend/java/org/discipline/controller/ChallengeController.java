package org.discipline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * OLTS on 20.09.2017.
 */
@Controller
public class ChallengeController {

    @RequestMapping(value = "/challenge/*", method = RequestMethod.GET)
    public String get(Model model) {
        // todo validation
        return "challenge";
    }
}