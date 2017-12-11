package com.korfax.simple_mongodb_and_springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Korfax on 2017-12-08.
 */
@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    @Value("${security.header.name:Authorization}")
    private String securityHeaderName;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest)request;

        final String accessToken = httpRequest.getHeader(securityHeaderName);
        if (accessToken != null) {
            User userFromToken = tokenService.getUserByToken(accessToken);
            if (userFromToken == null) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while parsing token");
                return;
            }

            UserDetails userFromDB = userDetailsService.loadUserByUsername(userFromToken.getUsername());
            if (userFromDB == null) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found by username from token");
                return;
            }

            if (!userFromToken.getPassword().equals(userFromDB.getPassword())) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bad credentials in token");
                return;
            }

            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userFromDB, userFromDB, userFromDB.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}