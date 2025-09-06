package com.example.dicegame.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private int targetScore = 25;
    private Dice dice = new Dice();
    private Http http = new Http();

    @Getter
    @Setter
    public static class Dice {
        private String mode;
    }

    @Getter
    @Setter
    public static class Http {
        private String url;
        private int timeoutMs = 2000;
    }
}
