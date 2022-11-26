package org.cyrilselyanin.vendingsystem.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class DefaultSecurityConfig {
//	@Value("${auth.rememberMeToken}")
//	private String rememberMeToken;
//
//	@Value("${auth.rememberMeParameter}")
//	private String rememberMeParameter;

//	@Autowired
//	RememberMeServices rememberMeServices;

	private static final String[] UNSECURED_RESOURCE_LIST = new String[] {
			"/resources/**", "/assets/**", "/css/**",
			"/webjars/**", "/images/**", "/dandelion/**", "/js/**"
	};

	private static final String[] UNAUTHORIZED_RESOURCE_LIST = new String[] {
			"/test.html", "/", "/unauthorized*",
			"/error*", "/users*", "/accessDenied"
	};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers(UNSECURED_RESOURCE_LIST);
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
						.antMatchers("/git", "/manage", "/manage/**")
							.hasRole("MANAGER")
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

//	@Bean
//	UserDetailsService users() {
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username("user1")
//				.password("password")
//				.roles("USER")
//				.build();
//		return new InMemoryUserDetailsManager(user);
//	}
}
