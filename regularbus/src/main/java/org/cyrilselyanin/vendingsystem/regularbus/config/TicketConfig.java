package org.cyrilselyanin.vendingsystem.regularbus.config;

import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.TicketCacheDto;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableConfigurationProperties(ConfigProps.class)
public class TicketConfig {

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	@Bean
	public Map<String, TicketCacheDto> ticketMap() {
		return new ConcurrentHashMap<>();
	}

}
