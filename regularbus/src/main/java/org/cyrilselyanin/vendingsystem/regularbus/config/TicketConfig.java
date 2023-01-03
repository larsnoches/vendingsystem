package org.cyrilselyanin.vendingsystem.regularbus.config;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
public class TicketConfig {

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	@Bean
	public Map<String, Optional<Ticket>> ticketMap() {
		return new HashMap<>();
	}

}
