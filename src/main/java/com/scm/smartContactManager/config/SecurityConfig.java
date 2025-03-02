package com.scm.smartContactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OA2AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthFailureHandler authenticationFailureHandler;

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

    /**
     * Provides an AuthenticationProvider to the Spring Security framework.
     * This Bean provides an implementation of the DaoAuthenticationProvider
     * which uses the BCryptPasswordEncoder to hash the password of the user
     * and the UserDetailsService to load the user details from the database.
     * 
     * @return an instance of AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider getAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    /**
     * This Bean provides an implementation of the SecurityFilterChain which
     * uses the following configuration:
     * <ul>
     * <li>Authorize all the URLs that start with "/user/" to be authenticated
     * and all the other URLs to be accessible without authentication.</li>
     * <li>Configure the formLogin to use the "/login" page for login and
     * "/authenticate" as the URL for the login processing. The default success
     * URL is set to "/user/dashboard" and the failure forward URL is set to
     * "/login?error=true".</li>
     * <li>Disable the Cross-Site Request Forgery (CSRF) protection.</li>
     * <li>Configure the logout to use the "/user/logout" URL for logout and
     * "/login?logout=true" as the success URL after logout.</li>
     * <li>Configure the OAuth2Login to use the "/login" page for login and
     * "/user/dashboard" as the default success URL after login.</li>
     * </ul>
     * 
     * @return an instance of SecurityFilterChain
     * @throws Exception if any error occurs while configuring the
     *                   SecurityFilterChain
     */
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
                        .passwordParameter("password")
                        .failureHandler(authenticationFailureHandler))

                .csrf(custom -> custom.disable())
                .logout(logoutForm -> logoutForm.logoutUrl("/user/logout")
                        .logoutSuccessUrl("/login?logout=true"))
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
