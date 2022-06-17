package com.df.service;

import com.df.entity.Owner;
import com.df.repository.OwnerRepository;
import com.df.request.OwnerRequest;
import com.df.util.OwnerUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public Mono<Owner> getById(Long id) {
        return validateId(id)
                .flatMap(ownerId ->
                        ownerRepository.findById(ownerId)
                                .switchIfEmpty(Mono.empty())
                );
    }

    private Mono<Long> validateId(Long id) {
        return Mono.justOrEmpty(id)
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<Owner> getAll() {
        return ownerRepository.findAll()
                .switchIfEmpty(Flux.empty());
    }

    @Override
    @Transactional
    public Mono<Owner> create(OwnerRequest ownerRequest) {
        return validateOwnerRequest(ownerRequest)
                .flatMap(validatedOwnerRequest ->
                        ownerRepository.save(OwnerUtility.ownerRequestToOwner(validatedOwnerRequest))
                                .doOnSuccess(o -> log.info("Created " + o + "."))
                                .doOnError(e -> log.info("Failed to create " + ownerRequest + ".", e))
                );
    }

    @Override
    @Transactional
    public Mono<Owner> deleteById(Long id) {
        return getById(id)
                .flatMap(owner ->
                        ownerRepository.delete(owner)
                                .doOnSuccess(o -> log.info("Deleted " + owner + "."))
                                .doOnError(e -> log.info("Failed to delete " + owner + ".", e))
                                .then(Mono.just(owner))
                );
    }

    @Override
    @Transactional
    public Mono<Owner> alter(OwnerRequest ownerRequest) {
        return validateOwnerRequest(ownerRequest)
                .flatMap(validatedOwnerRequest ->
                        getById(ownerRequest.getId())
                                .flatMap(currentOwner ->
                                        ownerRepository.save(OwnerUtility.ownerRequestToOwner(currentOwner, validatedOwnerRequest))
                                                .flatMap(owner -> Mono.just(owner))
                                                .doOnSuccess(o -> log.info("Altered " + validatedOwnerRequest + "."))
                                                .doOnError(e -> log.info("Failed to alter " + validatedOwnerRequest + ".", e))
                                )
                );
    }

    private Mono<OwnerRequest> validateOwnerRequest(OwnerRequest ownerRequest) {
        return Mono.justOrEmpty(ownerRequest)
                .switchIfEmpty(Mono.empty());
    }

}
