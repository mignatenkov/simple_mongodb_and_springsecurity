package com.korfax.simple_mongodb_and_springsecurity.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Korfax on 2017-12-04.
 */
@Service
public class TokenService {

    @Value("security.token.secret:changeit")
    private String secret;

    @Autowired
    private UserDetailsService userDetailsService;

    public String getTokenByCreds(String username, String password) throws AuthenticationCredentialsNotFoundException {
        if (username == null || password == null)
            return null;
        User user = (User) userDetailsService.loadUserByUsername(username);
        Map<String, Object> tokenData = new HashMap<String, Object>();
        if (password.equals(user.getPassword())) {
            tokenData.put("clientType", "user");
            tokenData.put("username", user.getUsername());
            tokenData.put("password", user.getPassword());
            tokenData.put("token_create_date", new Date().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 100);
            Date expirationDate = calendar.getTime();
            tokenData.put("token_expiration_date", expirationDate);
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(expirationDate);
            jwtBuilder.setClaims(tokenData);
            String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, secret).compact();
            return token;
        } else {
            throw new AuthenticationCredentialsNotFoundException("Authentication error");
        }
    }

    public User getUserByToken(String token) {
        Map<String, Object> tokenData = (Map) Jwts.parser()
                .setSigningKey(secret)
                .parse(token)
                .getBody();
        User retVal = new User(
                tokenData.get("username").toString(),
                tokenData.get("password").toString(),
                new ArrayList<GrantedAuthority>());
        return retVal;
    }

}