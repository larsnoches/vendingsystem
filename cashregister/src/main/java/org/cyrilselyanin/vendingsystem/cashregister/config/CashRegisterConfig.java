package org.cyrilselyanin.vendingsystem.cashregister.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CashRegisterConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

}
