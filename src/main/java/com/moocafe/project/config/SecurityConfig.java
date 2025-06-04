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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomAuthenticationSuccessHandler successHandler) throws Exception {
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
                                        "/inventory/**",
                                        "/check-code/**",
                                        "/favicon.ico").permitAll()
                                .requestMatchers("/headOffice/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/storeOwner/**").hasAnyRole("USER")
                                .anyRequest().authenticated()
                )
                .formLogin((form) ->
                        form.loginPage("/login")
                                .usernameParameter("userId")
                                .passwordParameter("userPw")
                                .loginProcessingUrl("/login")
                                .successHandler(successHandler)
                                //.defaultSuccessUrl("/index",true)
                                .failureUrl("/login?error")
                                .permitAll()
                ).requestCache(cache -> cache.requestCache(new HttpSessionRequestCache()))
                .logout((logout) ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/index")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                )
                .csrf((csrf)->csrf.disable());
        return httpSecurity.build();
    }

}