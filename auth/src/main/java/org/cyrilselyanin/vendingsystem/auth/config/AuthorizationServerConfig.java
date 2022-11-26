package org.cyrilselyanin.vendingsystem.auth.config;

import java.util.UUID;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthorizationServerConfig {

//	private final String AUTHORITIES_BY_USERNAME_QUERY =
//		"select username, authority from user_authorities " +
//		"inner join users on user_authorities.user_id = users.id " +
//		"inner join authorities on user_authorities.authority_id = authorities.id " +
//		"where username = ?";

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0

		http
				.exceptionHandling(exceptions ->
						exceptions.authenticationEntryPoint(
								new LoginUrlAuthenticationEntryPoint("/login")
						)
				)
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		return http.build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
//		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
		RegisteredClient registeredClient = RegisteredClient.withId("e4a295f7-0a5f-4cbc-bcd3-d870243d1b05")
				.clientId("vending_client")
//				.clientSecret("{noop}12345")
//				.clientSecret("{bcrypt}12345")
				.clientSecret(new BCryptPasswordEncoder().encode("12345"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.redirectUri("https://oidcdebugger.com/debug")
//				.redirectUri("http://127.0.0.1:8181/login/oauth2/code/messaging-client-oidc")
//				.redirectUri("http://127.0.0.1:4200/callback")
//				.redirectUri("http://127.0.0.1:8181/authorized")
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

//	@Bean
//	public EmbeddedDatabase embeddedDatabase() {
//		return new EmbeddedDatabaseBuilder()
//				.generateUniqueName(true)
//				.setType(EmbeddedDatabaseType.H2)
//				.setScriptEncoding("UTF-8")
//				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
//				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
//				.addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
//				.build();
//	}

	@Bean
	public RememberMeServices getRememberMeServices(
			JdbcUserDetailsManager jdbcUserDetailsManager,
			JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl,
			AuthProperties authProperties
	) {
//		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//		jdbcUserDetailsManager.setDataSource(dataSource);

//		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
//		jdbcTokenRepositoryImpl.setDataSource(dataSource);

		PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
				authProperties.getRememberMeToken(),
				jdbcUserDetailsManager,
				jdbcTokenRepositoryImpl
		);
		services.setParameter(authProperties.getRememberMeParameter());
		return services;
	}

	@Bean
	public JdbcUserDetailsManager getJdbcUserDetailsManager(DataSource dataSource)
	{
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
//		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);

//		jdbcUserDetailsManager.setUserExistsSql(
//				"select username from users where username = ?"
//		);
//		jdbcUserDetailsManager.setCreateUserSql(
//				"insert into users (username, password, enabled) values (?,?,?)"
//		);
//		jdbcUserDetailsManager.setCreateAuthoritySql(
//				"insert into authorities (username, authority) values (?,?)"
//		);
//		jdbcUserDetailsManager.setUpdateUserSql(
//				"update users set password = ?, enabled = ? where username = ?"
//		);
//		jdbcUserDetailsManager.setDeleteUserSql(
//				"delete from users where username = ?"
//		);
//		jdbcUserDetailsManager.setDeleteUserAuthoritiesSql(
//				"delete from authorities where username = ?"
//		);

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

//	@Bean
//	public AuthenticationManager getAuthenticationManager(
//			AuthenticationManagerBuilder builder,
//			JdbcUserDetailsManager jdbcUserDetailsManager,
////			PasswordEncoder passwordEncoder,
//			DataSource dataSource
//	) throws Exception {
//		return builder
//			.userDetailsService(jdbcUserDetailsManager)
////				.passwordEncoder(passwordEncoder)
//			.and()
//				.jdbcAuthentication()
////					.authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY)
////					.passwordEncoder(passwordEncoder)
//					.dataSource(dataSource)
//		.and().build();
//	}

}

