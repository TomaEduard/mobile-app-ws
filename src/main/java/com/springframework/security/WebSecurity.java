package com.springframework.security;

import com.springframework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //    Because AuthenticationFilter isn't a @Bean, can't been @Autowired into WebSecurity but we can create a new instance of it.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable().authorizeRequests()

                // public api for register
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()

                // public api for validated token and permit user to login
                .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL).permitAll()

                // public api for reset password generate token and sed email
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL).permitAll()

                // public api for reset password email with token + password will call this api
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL).permitAll()

                // public api for h2-database
                .antMatchers(SecurityConstants.H2_CONSOLE).permitAll()

                // public api for swagger
                .antMatchers("/v2/api-docs", "/configuration/**", "swagger-ui.html#/**" , "/swagger*/**", "/webjars/**").permitAll()

                // protect api
                // .anyRequest().authenticated().and().addFilter(new AuthenticationFilter(authenticationManager()));
                .anyRequest().authenticated().and().addFilter(getAuthenticationFilter())

                // validated token login
                .addFilter(new AuthorizationFilter(authenticationManager()))

                // prevent cached session and reauthorizing all request receive
                // we want all rest api(not login) contain header (Auth.. Bearer token) and reAuthorize
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

//        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8088"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/users/login", configuration);
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
