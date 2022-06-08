package com.df.service;

import reactor.core.publisher.Mono;

public interface AuditService {
    Mono<?> create(Object entityValue);
}
