package com.bond.librarymanager.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/")
    public String index() {

        return "UserPages/index";
    }

    @GetMapping("/login")
    public String login() {
        return "UserPages/login";
    }

    @GetMapping("/catalouge")
    @PreAuthorize("hasRole('USER')")
    public String catalouge() {



        return "UserPages/catalouge";
    }


    @GetMapping("/events")
    public String events() {
        return "UserPages/events";
    }

    @GetMapping("/activity")
    public String activity() {
        return "UserPages/activity";
    }
}
