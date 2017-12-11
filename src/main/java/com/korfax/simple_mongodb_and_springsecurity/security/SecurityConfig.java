package com.korfax.simple_mongodb_and_springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by Korfax on 2017-11-30.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    @Qualifier("userDetailsService")
    UserDetailsService userDetailsService;

/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        variant with open password storage
//        auth.userDetailsService(userDetailsService);

//        variant with password encoding
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

//        InMemory variant of userStorage
//        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("user1").password("user").roles("USER");
    }
*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class)
            .authorizeRequests()
                .antMatchers("/users/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                .antMatchers("/login").access("permitAll()")
//            .and().formLogin()
            .and().csrf().disable();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder;
//    }

}
