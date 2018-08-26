package br.com.kimae.multitenacydemo.config.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import br.com.kimae.multitenacydemo.persistence.app.AppUser;
import br.com.kimae.multitenacydemo.persistence.app.AppUserRepository;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    private void saveTestUser() {
        final AppUser user1 = new AppUser();
        user1.setEmail("email-cliente1@email.com");
        user1.setPassword("pass");
        user1.setClientDatabase("cliente1");
        appUserRepository.save(user1);

        final AppUser user2 = new AppUser();
        user2.setEmail("email-cliente2@email.com");
        user2.setPassword("pass");
        user2.setClientDatabase("cliente2");
        appUserRepository.save(user2);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        dao.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(dao);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/static/**").permitAll()
            .anyRequest().authenticated()

            .and()
            .formLogin().
            loginPage("/login").permitAll().
            loginProcessingUrl("/doLogin")

            .and()
            .logout().permitAll().logoutUrl("/logout")

            .and()
            .csrf().disable()
        ;
    }

}
