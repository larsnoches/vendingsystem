package org.cyrilselyanin.vendingsystem.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class DefaultSecurityConfig {

	private static final String[] UNSECURED_RESOURCE_LIST = new String[] {
			"/resources/**", "/assets/**", "/css/**",
			"/webjars/**", "/images/**", "/js/**"
	};

	private static final String[] UNAUTHORIZED_RESOURCE_LIST = new String[] {
			"/", "/unauthorized*", "/login",
			"/oauth2/authorize",
//			"/userinfo",
			"/error*", "/users*", "/accessDenied"
	};

	private static final String[] MANAGER_ONLY_RESOURCE_LIST = new String[] {
			"/manage", "/manage/**"
	};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web
				.ignoring().antMatchers(UNSECURED_RESOURCE_LIST);
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(
			HttpSecurity http,
			RememberMeServices rememberMeServices,
			AuthProperties authProperties
	) throws Exception {

//		http
//				.authorizeHttpRequests(authorize ->
//						authorize.anyRequest().authenticated()
//				)
//				.formLogin(withDefaults());
//		return http.build();



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
//						.antMatchers(HttpMethod.OPTIONS, "/userinfo")
//							.permitAll()
						.anyRequest()
							.authenticated()
				.and()
					.formLogin()
//						.loginPage("/login")
						.permitAll()
				.and()
					.headers()
						.cacheControl()
					.and()
						.frameOptions()
							.deny();

//				.and()
//					.exceptionHandling()
//						.accessDeniedPage("/access?error")
//				.and()
//					.rememberMe()
//						.useSecureCookie(true)
//						.tokenValiditySeconds(60 * 60 * 24 * 10) // 10 days
//						.rememberMeServices(rememberMeServices)
//						.key(authProperties.getRememberMeToken())
//				.and()
//					.logout()
//						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//						.logoutSuccessUrl("/?logout")
//				.and()
//					.sessionManagement()
//						.maximumSessions(1)
//						.expiredUrl("/login?expired");

		return http.build();
	}
}
