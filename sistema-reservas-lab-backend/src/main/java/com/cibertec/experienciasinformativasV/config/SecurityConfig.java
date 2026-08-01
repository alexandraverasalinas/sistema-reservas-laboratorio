package com.cibertec.experienciasinformativasV.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/**").permitAll()
	            .requestMatchers("/api/admin/**").permitAll()
	            .requestMatchers("/**").permitAll()
	            .anyRequest().permitAll()
	        )
	        .formLogin(form -> form.disable())
	        .httpBasic(basic -> basic.disable());

	    return http.build();
	}
}