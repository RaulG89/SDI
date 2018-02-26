package com.uniovi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests() // peticiones autorizadas
				.antMatchers("/css/**", "/img/**", "/script/**", "/", "/signup",
						"/login/**")
				// Permite a todos los usuarios
				.permitAll().anyRequest().authenticated()
				// Especifica que usuarios pueden usar esas webs
				// Orden de más espeficica a maás general
				.antMatchers("/mark/edit/*").hasAuthority("ROLE_PROFESSOR")
				.antMatchers("/mark/delete/*").hasAuthority("-ROLE_PROFESSOR")
				.antMatchers("/mark/**")
				.hasAnyAuthority("ROLE_STUDENT", "ROLE_PROFESSOR", "ROLE_ADMIN")
				.antMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN")
				.anyRequest().authenticated().and()
				// pagina de autentificacion por defecto
				.formLogin().loginPage("/login").permitAll()
				// Si se loguea bien
				.defaultSuccessUrl("/home").and().logout().permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder());
	}
}