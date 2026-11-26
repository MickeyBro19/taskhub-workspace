package com.mickey.taskhub.config;

import com.mickey.taskhub.security.CustomUserDetailsService;
import com.mickey.taskhub.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final CustomUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	public SecurityConfig(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
		return new ProviderManager(authenticationProvider);
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
	                                               AuthenticationProvider authenticationProvider,
	                                               AuthenticationManager authenticationManager, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.logout(AbstractHttpConfigurer::disable)
				.sessionManagement(session->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(e->e.authenticationEntryPoint(
						new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
				                                                ))
				.authenticationProvider(authenticationProvider)
				.authorizeHttpRequests(auth->auth
						                       .requestMatchers(
								                       HttpMethod.POST,
								                       "/api/users/register",
								                       "/api/auth/login"
						                                       ).permitAll()
						                       
						                       .requestMatchers("/actuator/health").permitAll()
						                       
						                       .anyRequest().authenticated()
				                      )
				.addFilterBefore(
						jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class
				                );
		return http.build();
	}
}

