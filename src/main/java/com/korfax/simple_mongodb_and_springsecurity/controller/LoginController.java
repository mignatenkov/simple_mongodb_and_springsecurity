package com.korfax.simple_mongodb_and_springsecurity.controller;

import com.korfax.simple_mongodb_and_springsecurity.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Korfax on 2017-12-11.
 */
@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private TokenService tokenService;

    @Value("${security.header.name:Authorization}")
    private String securityHeaderName;

    @RequestMapping("/login")
    public ResponseEntity helloAdmin(@RequestParam("username") String username,
                             @RequestParam("password") String password) {
        try {
            String retVal = tokenService.getTokenByCreds(username, password);
            log.debug("Generated token: "+ retVal);
            return ResponseEntity.ok().header(securityHeaderName, retVal).body(retVal);
        } catch(AuthenticationCredentialsNotFoundException acnfe) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
