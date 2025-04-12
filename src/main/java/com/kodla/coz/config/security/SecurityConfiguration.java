package com.kodla.coz.config.security;

import com.kodla.coz.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final UserRepository userRepository;

    public SecurityConfiguration(DataSource dataSource, AuthenticationSuccessHandler authenticationSuccessHandler, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/admin/**").access("hasAuthority('ADMIN')");

        http.csrf().ignoringAntMatchers("/compile", "/feedback", "/sitemap.xml")
                .and()
                .authorizeRequests()
                .antMatchers("/","/**/favicon.ico", "/css/**", "/js/**", "/images/**", "/login", "/register", "/verify", "/reset_password", "/forgot_password", "/user-photos/**", "/compile", "/feedback", "/actuator/**", "/sitemap.xml")
                .permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();

        // http.authorizeRequests().antMatchers("/compile").permitAll().and().csrf().disable().cors();

        http.formLogin()
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .usernameParameter("email")
                .loginProcessingUrl("/login")
                .failureUrl("/login?loginFailed=true")
                .and()
                .logout().logoutSuccessUrl("/login?logout=true").permitAll();
        // http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
        http.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
