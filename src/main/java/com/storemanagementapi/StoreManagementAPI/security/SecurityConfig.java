package com.storemanagementapi.StoreManagementAPI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("123admin"))
                .roles("ADMIN")
                .and()
                .withUser("siteuser").password(passwordEncoder().encode("123user"))
                .roles("SITEUSER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/products/**").hasAnyRole("SITEUSER", "ADMIN")
                .antMatchers(HttpMethod.PUT,"/api.products/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/products/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/products/**").hasAnyRole("ADMIN")
                .and()
                .logout()
                .and()
                .csrf().disable()
                .httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
