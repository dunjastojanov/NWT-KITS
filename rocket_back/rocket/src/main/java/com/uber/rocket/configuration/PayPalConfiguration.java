package com.uber.rocket.configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConfiguration {
    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public Map<String, String> payPalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }


    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }


}
