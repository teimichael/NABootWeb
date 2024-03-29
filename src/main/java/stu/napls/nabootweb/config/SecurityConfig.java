package stu.napls.nabootweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import stu.napls.nabootweb.config.property.AppServer;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AppServer appServer;


    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .httpBasic().disable()
                .csrf().disable();
    }

}