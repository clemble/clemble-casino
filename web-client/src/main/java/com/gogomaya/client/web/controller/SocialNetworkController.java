package com.gogomaya.client.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Component
public class SocialNetworkController {

    public SocialNetworkController() {
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "index";
    }

    @RequestMapping(value = "view/{signout}", method = RequestMethod.GET)
    public String showView(@PathVariable("signout") String signout) {
        return signout;
    }

    @RequestMapping(value = "/{signout}", method = RequestMethod.GET)
    public String signout(String view) {
        return view;
    }

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    public String signout(Model model, HttpServletRequest httpRequest) {
        return "signin";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest httpRequest) {
        return "signin";
    }

}
