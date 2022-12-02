package org.cyrilselyanin.vendingsystem.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.cyrilselyanin.vendingsystem.auth.config.jose.Jwks;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthorizationServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(
			HttpSecurity http
	) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0

		http
				.exceptionHandling(exceptions ->
						exceptions.authenticationEntryPoint(
								new LoginUrlAuthenticationEntryPoint("/login")
						)
				)
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.cors();

		return http.build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(
			JdbcTemplate jdbcTemplate,
			AuthProperties authProperties
	) {
//		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
		RegisteredClient registeredClient = RegisteredClient.withId("e4a295f7-0a5f-4cbc-bcd3-d870243d1b05")
				.clientId("vending_client")
//				.clientSecret(new BCryptPasswordEncoder().encode("12345"))
				.clientSecret(
						new BCryptPasswordEncoder().encode(
								authProperties.getClientSecret()
						)
				)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)

				.redirectUri("https://oidcdebugger.com/debug")
				.redirectUri("http://127.0.0.1:8181/login/oauth2/code/messaging-client-oidc")
				.redirectUri("http://127.0.0.1:8181/authorized")

				.redirectUri(
						String.format(
								"http://%s:%s",
								authProperties.getClientRedirectHost(),
								authProperties.getClientRedirectPort()
						)
				)
				.redirectUri(
						String.format(
								"http://%s:%s/callback",
								authProperties.getClientRedirectHost(),
								authProperties.getClientRedirectPort()
						)
				)

				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.scope("vending.read")
				.scope("vending.write")
				.clientSettings(
						ClientSettings.builder()
								.requireAuthorizationConsent(true)
								.build()
				)
				.build();

		// Save registered client in db as if in-memory
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(
				jdbcTemplate
		);
		registeredClientRepository.save(registeredClient);

		return registeredClientRepository;
	}

	@Bean
	public OAuth2AuthorizationService authorizationService(
			JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository
	) {
		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
	}

	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(
			JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository
	) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	@Bean
	public RememberMeServices getRememberMeServices(
			JdbcUserDetailsManager jdbcUserDetailsManager,
			JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl,
			AuthProperties authProperties
	) {
		PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
				authProperties.getRememberMeToken(),
				jdbcUserDetailsManager,
				jdbcTokenRepositoryImpl
		);
		services.setParameter(authProperties.getRememberMeParameter());
		return services;
	}

	@Bean
	public JdbcUserDetailsManager getJdbcUserDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		return jdbcUserDetailsManager;
	}

	@Bean
	public JdbcTokenRepositoryImpl getJdbcTokenRepositoryImpl(DataSource dataSource) {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(
				"http://127.0.0.1:4200",
				"http://127.0.0.1:4200/*",
				"http://127.0.0.1:4200/**",
				"http://127.0.0.1:4200/",
				"http://127.0.0.1:4200/callback",
				"http://127.0.0.1:8181/",
				"http://127.0.0.1:8181/*",
				"http://127.0.0.1:8181/**",
				"http://127.0.0.1:8181",
				"http://localhost",
				"http://localhost:4200",
				"http://192.168.9.2:4200/callback",
				"http://192.168.9.2:4200/",
				"http://192.168.9.2:4200",
				"http://127.0.0.1:8181/login",
				"http://127.0.0.1:8181/userinfo"
		));
		configuration.setAllowedHeaders(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList(
				"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
		));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}

