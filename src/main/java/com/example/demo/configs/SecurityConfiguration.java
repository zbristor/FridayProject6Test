package com.example.demo.configs;

import org.springframework.context.annotation.Configuration;
import com.example.demo.models.SSUserDetailsService;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/register").permitAll()
                //.antMatchers("/seekersearch","/recreuitUserSearch","/recruitSchoolSearch","/recruitSkillSearch","/recruitCompanySearch","/recruiterjobsearch","/jobresults").hasAuthority("USER")
                .antMatchers("/recruiterjobpost","/recruitSkillSearch").hasAuthority("ADMIN")
                .antMatchers("/recreuitUserSearch","/recruitSchoolSearch","/recruitCompanySearch","/jobresults").authenticated()
                .anyRequest().authenticated()//"/seekersearch","/recruiterjobsearch"-->
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER")
                .and()
                .withUser("root").password("password").roles("ADMIN");
        auth
                .userDetailsService(userDetailsServiceBean())
                .passwordEncoder(encoder());
    }
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new SSUserDetailsService(userRepository);
    }
}