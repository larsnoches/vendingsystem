package org.cyrilselyanin.vendingsystem.cashregister.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Getter
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "cashregister")
public class ConfigProps {

	private String mqExchange;
	private String mqQueue;
	private String mqRoutingKey;
	private String sbisAuthAppClientId;
	private String sbisAuthAppSecret;
	private String sbisAuthSecretKey;

}
