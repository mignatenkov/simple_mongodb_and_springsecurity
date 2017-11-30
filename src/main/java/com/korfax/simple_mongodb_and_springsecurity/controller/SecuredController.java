package com.korfax.simple_mongodb_and_springsecurity.controller;

import com.korfax.simple_mongodb_and_springsecurity.service.UserService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Korfax on 2017-11-30.
 */
@RestController
public class SecuredController {

    private static final Logger log = LoggerFactory.getLogger(SecuredController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/users")
    public String helloAdmin() {
        return "Users:<br>" + userService.getAllUsers();
    }

    @RequestMapping("/user/{username}")
    public String helloUser(@PathParam("username") String username) {
        return "Greetings, " + userService.getUserByUsername(username) + "!";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody String createUser(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("roles") String roles) {
        log.debug("POST Params: username="+ username + ", password="+ password +", roles=" + roles);
        userService.createUser(username, password, Arrays.stream(roles.substring(1, roles.length()-1).split(",")).collect(Collectors.toList()));
        return Optional.ofNullable(userService.getUserByUsername(username)).orElse(new JSONObject()).toString();
    }

}
