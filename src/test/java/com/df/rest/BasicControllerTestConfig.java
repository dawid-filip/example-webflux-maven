package com.df.rest;

import com.df.dto.OwnerDto;
import com.df.dto.PetDto;
import com.df.entity.Owner;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@ExtendWith(SpringExtension.class)
abstract class BasicControllerTestConfig {

    @Autowired
    protected WebTestClient webClient;

    protected ParameterizedTypeReference<List<PetDto>> getListPetDtoType() {
        return new ParameterizedTypeReference<>(){};
    }

    protected ParameterizedTypeReference<List<Owner>> getListOwnerType() {
        return new ParameterizedTypeReference<>(){};
    }

    protected ParameterizedTypeReference<List<OwnerDto>> getListOwnerDtoType() {
        return new ParameterizedTypeReference<>(){};
    }

}
