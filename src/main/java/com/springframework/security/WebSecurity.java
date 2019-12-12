package com.springframework.security;

import com.springframework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import sun.security.util.SecurityConstants;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //    Because AuthenticationFilter isn't a @Bean, can't been @Autowired into WebSecurity but we can create a new instance of it.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable().authorizeRequests()
        // public api for register
        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
        .permitAll()
        // public api for validated token and permit user to login
        .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
        .permitAll()

        // protect api
        // .anyRequest().authenticated().and().addFilter(new AuthenticationFilter(authenticationManager()));
         .anyRequest().authenticated().and().addFilter(getAuthenticationFilter())

         // validated token login
         .addFilter(new AuthorizationFilter(authenticationManager()))

         // prevent cached session and reauthorizing all request receive
         // we want all rest api(not login) contain header (Auth.. Bearer token) and reAuthorize
         .sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }

}
