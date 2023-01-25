package com.uber.rocket.configuration;

import com.uber.rocket.entity.user.RoleType;
import com.uber.rocket.security.CustomAuthorizationFilter;
import com.uber.rocket.service.AuthService;
import com.uber.rocket.service.CustomUserDetailsService;
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


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AuthService authService;

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
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/logged").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/random/admin").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/user/**").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user").hasAnyAuthority(RoleType.ADMINISTRATOR.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/image/**").hasAnyAuthority(RoleType.CLIENT.name(), RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/user/**").hasAnyAuthority(RoleType.ADMINISTRATOR.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/vehicle/**").hasAnyAuthority(RoleType.ADMINISTRATOR.name(), RoleType.DRIVER.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/vehicle").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/ride/**").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/payment/**").hasAnyAuthority(RoleType.CLIENT.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/notification/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/confirm/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/images/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/password").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/login").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/chat").hasAnyAuthority(RoleType.ADMINISTRATOR.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/chat").hasAnyAuthority(RoleType.ADMINISTRATOR.name(),RoleType.CLIENT.name());
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/chat/message").hasAnyAuthority(RoleType.ADMINISTRATOR.name(),RoleType.CLIENT.name());
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new CustomAuthorizationFilter(authService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}