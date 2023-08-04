package br.com.kimae.multitenacydemo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.kimae.multitenacydemo.persistence.app.AppUser;
import br.com.kimae.multitenacydemo.persistence.app.AppUserRepository;
import jakarta.annotation.PostConstruct;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final AppUserRepository appUserRepository;

	private final UserDetailsService userDetailsService;

	public SecurityConfig(AppUserRepository appUserRepository, UserDetailsService userDetailsService) {
		this.appUserRepository = appUserRepository;
		this.userDetailsService = userDetailsService;
	}

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

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						authz -> authz.requestMatchers("/static/**").permitAll().anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.permitAll()
						.loginProcessingUrl("/doLogin")
				)
				.logout(form -> form.logoutUrl("/logout"))
				.csrf(csrf -> csrf.disable())
				.build();
	}
}
