package org.cyrilselyanin.vendingsystem.regularbus.config;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class TicketConfig {

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	@Bean
	public Map<String, Ticket> ticketMap() {
		return new ConcurrentHashMap<>();
	}

}
