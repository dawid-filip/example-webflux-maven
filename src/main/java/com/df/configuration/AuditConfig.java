package com.df.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Configuration
@EnableR2dbcAuditing(auditorAwareRef = "reactiveAuditorAware", dateTimeProviderRef = "dateTimeProvider")
public class AuditConfig {

    @Bean
    public ReactiveAuditorAware<String> reactiveAuditorAware() {
        return () -> Mono.just("RAND-" + generateRandomUser());
    }

    private String generateRandomUser() {
        return new Random().ints(97 /* a */, 122 /* z */ + 1)
                .limit(5)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }

}