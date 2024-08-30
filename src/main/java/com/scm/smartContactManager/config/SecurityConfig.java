package com.scm.smartContactManager.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm.smartContactManager.services.impl.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OA2AuthenticationSuccessHandler authenticationSuccessHandler;

    // @Autowired
    // private SecurityCustomUserDetailService secCustomUserDetailService;

    // @Bean
    // public UserDetailsService userDetailsService() {
    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("root")
    // .password("root")
    // .roles("ADMIN", "USER")
    // .build();

    // UserDetails user2 = User.withUsername("root123")
    // .password("root123")
    // .roles("ADMIN")
    // .build();
    // return new InMemoryUserDetailsManager(user1, user2);
    // }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/user/**").authenticated()
                                .anyRequest()
                                .permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticate")
                        .defaultSuccessUrl("/user/dashboard")
                        .failureForwardUrl("/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password"))

                .csrf(custom -> custom.disable())
                .logout(logoutForm -> logoutForm.logoutUrl("/user/logout").logoutSuccessUrl("/login?logout=true"))
                .oauth2Login(oauth -> oauth.defaultSuccessUrl("/user/dashboard")
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler))
                .build();

        // .failureHandler(new AuthenticationFailureHandler() {

        // @Override
        // public void onAuthenticationFailure(HttpServletRequest request,
        // HttpServletResponse response, AuthenticationException exception)
        // throws IOException, ServletException {
        // // TODO Auto-generated method stub
        // throw new UnsupportedOperationException(
        // "Unimplemented method 'onAuthenticationFailure'");
        // }

        // })
        // .successHandler(new AuthenticationSuccessHandler() {

        // @Override
        // public void onAuthenticationSuccess(HttpServletRequest request,
        // HttpServletResponse response, Authentication authentication)
        // throws IOException, ServletException {
        // // TODO Auto-generated method stub
        // throw new UnsupportedOperationException(
        // "Unimplemented method 'onAuthenticationSuccess'");
        // }

        // })

    }

}
