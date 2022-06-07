package com.df.service;

import com.df.dto.OwnerDto;
import reactor.core.publisher.Flux;

public interface OwnerDtoService {
    Flux<OwnerDto> getAll();
}
