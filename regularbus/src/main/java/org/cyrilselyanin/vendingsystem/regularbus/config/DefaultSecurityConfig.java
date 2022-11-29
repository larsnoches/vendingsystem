package org.cyrilselyanin.vendingsystem.regularbus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
//		http
//				.authorizeHttpRequests(authz ->
//						authz.anyRequest().authenticated()
//				)
//				.oauth2ResourceServer().jwt();

		http
				.headers()
					.frameOptions()
						.sameOrigin()
				.and()
					.authorizeRequests()
						.antMatchers(UNAUTHORIZED_RESOURCE_LIST)
							.permitAll()
						.antMatchers(MANAGER_ONLY_RESOURCE_LIST)
							.hasRole("MANAGER")
						.anyRequest()
							.authenticated()
				.and()
					.oauth2ResourceServer().jwt();

		return http.build();
	}
}
