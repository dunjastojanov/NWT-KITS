package com.uber.rocket.configuration;

import com.uber.rocket.entity.user.RoleType;
import com.uber.rocket.security.CustomAuthenticationFilter;
import com.uber.rocket.security.CustomAuthorizationFilter;
import com.uber.rocket.service.AuthService;
import com.uber.rocket.service.CustomUserDetailsService;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(daoAuthenticationProvider(), authService, userService);
        authenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/logged").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/user/**").hasAnyAuthority(RoleType.CLIENT.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user").hasAnyAuthority(RoleType.ADMINISTRATOR.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/vehicle/**").hasAnyAuthority(RoleType.ADMINISTRATOR.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/image/**").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/user/**").hasAnyAuthority(RoleType.ADMINISTRATOR.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/vehicle").hasAnyAuthority(RoleType.ADMINISTRATOR.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/vehicle/**").hasAnyAuthority(RoleType.DRIVER.name());

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/confirm/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomAuthorizationFilter(authService), UsernamePasswordAuthenticationFilter.class);
        http.cors();
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200/", "http://127.0.0.1:80/", "http://example.com/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "OPTIONS", "PUT", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
