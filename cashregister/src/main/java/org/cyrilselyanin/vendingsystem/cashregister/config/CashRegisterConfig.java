package org.cyrilselyanin.vendingsystem.cashregister.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ConfigProps.class)
public class CashRegisterConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

}
