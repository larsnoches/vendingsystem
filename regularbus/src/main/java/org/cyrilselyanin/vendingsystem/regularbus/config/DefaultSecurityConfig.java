//package org.cyrilselyanin.vendingsystem.regularbus.config;
//
//import org.cyrilselyanin.vendingsystem.regularbus.helper.JwtRoleConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class DefaultSecurityConfig {
//
//	private static final String[] UNAUTHORIZED_RESOURCE_LIST = new String[] {
//			"/",
//	};
//
//	private static final String[] MANAGER_ONLY_RESOURCE_LIST = new String[] {
//			"/carriers", "/carriers/**"
//	};
//
//	@Bean
//	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
////		http
////				.authorizeHttpRequests(authz ->
////						authz.anyRequest().authenticated()
////				)
////				.oauth2ResourceServer().jwt();
//		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());
//
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
//					.oauth2ResourceServer()
//						.jwt()
//						.jwtAuthenticationConverter(jwtAuthenticationConverter);
//
//		http.cors();
//
//		return http.build();
//	}
//
//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList(
//				"http://127.0.0.1:4200",
//				"http://127.0.0.1:4200/*",
//				"http://127.0.0.1:4200/**",
//				"http://127.0.0.1:4200/",
//				"http://127.0.0.1:4200/callback",
//				"http://127.0.0.1:8181/",
//				"http://127.0.0.1:8181/*",
//				"http://127.0.0.1:8181/**",
//				"http://127.0.0.1:8181",
//				"http://localhost",
//				"http://localhost:4200",
//				"http://192.168.9.2:4200/callback",
//				"http://192.168.9.2:4200/",
//				"http://192.168.9.2:4200",
//				"http://127.0.0.1:8181/login",
//				"http://127.0.0.1:8181/userinfo"
//		));
//		configuration.setAllowedHeaders(Arrays.asList("*"));
////		configuration.setAllowedMethods(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList(
//				"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
//		));
//		configuration.setAllowCredentials(true);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
//}
