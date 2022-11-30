package org.cyrilselyanin.vendingsystem.regularbus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class DefaultSecurityConfig {

	private static final String[] UNAUTHORIZED_RESOURCE_LIST = new String[] {
			"/",
	};

	private static final String[] MANAGER_ONLY_RESOURCE_LIST = new String[] {
			"/carriers", "/carriers/**"
	};

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authz ->
						authz.anyRequest().authenticated()
				)
				.oauth2ResourceServer().jwt();


//		http
//				.headers()
//					.frameOptions()
//						.sameOrigin()
//				.and()
//					.authorizeRequests()
//						.antMatchers(UNAUTHORIZED_RESOURCE_LIST)
//							.permitAll()
//						.antMatchers(MANAGER_ONLY_RESOURCE_LIST)
//							.hasRole("MANAGER")
//						.anyRequest()
//							.authenticated()
//				.and()
//					.oauth2ResourceServer().jwt();

		http.cors();

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(
				"*"
//				"http://localhost",
//				"http://localhost:4200"
		));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
