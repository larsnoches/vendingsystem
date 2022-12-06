package org.cyrilselyanin.vendingsystem.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Getter
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

	private String rememberMeToken;
	private String rememberMeParameter;
	private String clientSecret;
	private String clientRedirectHost;
	private String clientRedirectPort;
//	private String clientUserRegistrationUrl;
	private String clientUserDefaultUrl;

}
