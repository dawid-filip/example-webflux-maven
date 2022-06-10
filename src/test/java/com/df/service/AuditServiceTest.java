package com.df.service;

import com.df.entity.Audit;
import com.df.entity.BasicAudit;
import com.df.entity.Pet;
import com.df.repository.AuditRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@Log4j2
public class AuditServiceTest {

    @InjectMocks
    public AuditServiceImpl auditServiceImpl;

    @Mock
    public AuditRepository auditRepository;

    @Test
    public void testCreate() {
        BasicAudit entity = Pet.builder().id(1L).name("petNam1").age((short) 1).weight((short) 2).length((short) 11).build();
        Audit audit = Audit.builder().id(null).entityValue(entity.toString()).entityClass(entity.getClass().getName())
                .auditedOn(LocalDateTime.now(ZoneOffset.UTC)).build();

        Mockito.when(auditRepository.save(any())).thenReturn(Mono.just(audit)); // any() because of 'random' auditedOn timestamp

        StepVerifier.create(auditServiceImpl.create(entity))
                .expectNextMatches(auditResult ->
                        auditResult.getEntityValue().equalsIgnoreCase(entity.toString()) &&
                                auditResult.getClass().getName().equalsIgnoreCase(audit.getClass().getName()) &&
                                Objects.nonNull(auditResult.getAuditedOn())
                )
                .verifyComplete();

        Mockito.verify(auditRepository, times(1)).save(any());
    }

    @Test
    public void testCreateAllOne() {
        BasicAudit entity = Pet.builder().id(1L).name("petNam1").age((short) 1).weight((short) 2).length((short) 11).build();
        Audit audit = Audit.builder().id(null).entityValue(entity.toString()).entityClass(entity.getClass().getName())
                .auditedOn(LocalDateTime.now(ZoneOffset.UTC)).build();

        Mockito.when(auditRepository.saveAll(anyList())).thenReturn(Flux.fromIterable(List.of(audit)));

        StepVerifier.create(auditServiceImpl.createAll(List.of(entity)))
                .expectNextMatches(auditResult ->
                        auditResult.getEntityValue().equalsIgnoreCase(entity.toString()) &&
                                auditResult.getClass().getName().equalsIgnoreCase(audit.getClass().getName()) &&
                                Objects.nonNull(auditResult.getAuditedOn())
                )
                .verifyComplete();

        Mockito.verify(auditRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testCreateTwo() {
        BasicAudit entity1 = Pet.builder().id(1L).name("petNam1").age((short) 1).weight((short) 2).length((short) 11).build();
        BasicAudit entity2 = Pet.builder().id(2L).name("petNam2").age((short) 2).weight((short) 3).length((short) 12).build();

        Audit audit1 = Audit.builder().id(null).entityValue(entity1.toString()).entityClass(entity1.getClass().getName())
                .auditedOn(LocalDateTime.now(ZoneOffset.UTC)).build();
        Audit audit2 = Audit.builder().id(null).entityValue(entity2.toString()).entityClass(entity2.getClass().getName())
                .auditedOn(LocalDateTime.now(ZoneOffset.UTC)).build();

        Mockito.when(auditRepository.saveAll(anyList())).thenReturn(Flux.fromIterable(List.of(audit1, audit2)));

        StepVerifier.create(auditServiceImpl.createAll(List.of(entity1, entity2)))
                .expectNextMatches(auditResult ->
                        auditResult.getEntityValue().equalsIgnoreCase(entity1.toString()) &&
                                auditResult.getClass().getName().equalsIgnoreCase(audit1.getClass().getName()) &&
                                Objects.nonNull(auditResult.getAuditedOn())
                )
                .expectNextMatches(auditResult ->
                        auditResult.getEntityValue().equalsIgnoreCase(entity2.toString()) &&
                                auditResult.getClass().getName().equalsIgnoreCase(audit2.getClass().getName()) &&
                                Objects.nonNull(auditResult.getAuditedOn())
                )
                .verifyComplete();

        Mockito.verify(auditRepository, times(1)).saveAll(anyList());
    }

}
