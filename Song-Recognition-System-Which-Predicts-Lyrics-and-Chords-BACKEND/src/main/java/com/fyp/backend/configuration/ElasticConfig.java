package com.fyp.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("elasticsearch")
@Getter
@Setter
public class ElasticConfig {

    private String host;
    private String user;
    private String pass;
}

