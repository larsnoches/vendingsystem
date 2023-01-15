package org.cyrilselyanin.vendingsystem.regularbus.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Getter
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "regularbus")
public class ConfigProps {

	private String mqExchange;
	private String mqQueue;
	private String mqRoutingKey;

}
