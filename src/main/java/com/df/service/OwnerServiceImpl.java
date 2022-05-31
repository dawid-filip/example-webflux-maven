package com.df.service;

import com.df.entity.Owner;
import com.df.repository.OwnerRepository;
import com.df.request.OwnerRequest;
import com.df.util.OwnerUtility;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Log4j2
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public Mono<Owner> getById(Long id) {
        return ownerRepository.findById(id)
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
        return ownerRepository.save(OwnerUtility.ownerRequestToOwner(ownerRequest))
                .doOnSuccess(o -> log.info("Created " + o + "."))
                .doOnError(o -> log.info("Failed to create " + o + "."));
    }

    @Override
    @Transactional
    public Mono<Owner> deleteById(Long id) {
        return getById(id)
                .flatMap(owner ->
                        ownerRepository.deleteById(id)
                                .doOnSuccess(o -> log.info("Deleted " + owner + "."))
                                .doOnError(o -> log.info("Failed to delete " + owner + "."))
                                .then(Mono.just(owner))
                );
    }

    @Override
    @Transactional
    public Mono<Owner> alter(OwnerRequest ownerRequest) {
        return getById(ownerRequest.getId())
                .flatMap(currentOwner ->
                        ownerRepository.save(OwnerUtility.ownerRequestToOwner(ownerRequest))
                                .flatMap(owner -> Mono.just(owner))
                                .doOnSuccess(o -> log.info("Altered " + ownerRequest + "."))
                                .doOnError(o -> log.info("Failed to alter " + ownerRequest + "."))
                );
    }

}
