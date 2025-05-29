package com.moocafe.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((auth) ->
                        auth.requestMatchers(
                                        "/",
                                        "/index",
                                        "/brand/**",
                                        "/css/**",
                                        "/images/**",
                                        "/include/**",
                                        "/js/**",
                                        "/html/**",
                                        "/upload/**",
                                        "/api/**",
                                        "/favicon.ico").permitAll()
                                //.requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin((form) ->
                        form.loginPage("/brand/login")
                                .usernameParameter("user_id")
                                .passwordParameter("user_pw")
                                .loginProcessingUrl("/brand/login")
                                //   .successHandler(successHandler)
                                .defaultSuccessUrl("/index",true)
                                .failureUrl("/brand/login?error")
                                .permitAll()
                ).requestCache(cache -> cache.requestCache(new HttpSessionRequestCache()))
                .logout((logout) ->
                        logout.logoutUrl("/brand/logout")
                                .logoutSuccessUrl("/index")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                )
                .csrf((csrf)->csrf.disable());
        return httpSecurity.build();
    }

}