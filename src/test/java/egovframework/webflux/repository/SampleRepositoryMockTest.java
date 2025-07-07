package egovframework.webflux.repository;

import egovframework.webflux.entity.Sample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.publisher.Mono.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
class SampleRepositoryMockTest {

//    @Mock
    @Autowired
    private SampleRepository sampleRepository;

    @BeforeEach
    void setUp() {
//        sampleRepository = Mockito.mock(SampleRepository.class);
    }

    @Test
    public void count() {
        // given
        List<Sample> mockSamples = Arrays.asList(
                new Sample("id1", "SAMPLE-001", "Name1", "Desc1", "Y", "User1"),
                new Sample("id2", "SAMPLE-002", "Name2", "Desc2", "Y", "User2"),
                new Sample("id3", "SAMPLE-003", "Name3", "Desc3", "Y", "User3")
        );

        // mocking
        Mono<Flux<Sample>> fluxMono = when(sampleRepository.selectAllSample()).thenReturn(Flux.fromIterable(mockSamples));

        // when
        Flux<Sample> result = sampleRepository.selectAllSample();

        // then
        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void content() {
        Flux<Sample> result = sampleRepository.selectAllSample();


        StepVerifier.create(result)
                .expectNextMatches(sample ->
                        "SAMPLE-00008".equals(sample.getSampleId()) &&
                        "Test Tool".equals(sample.getDescription())
                )
                .expectNextMatches(sample ->
                        "SAMPLE-00007".equals(sample.getSampleId()) &&
                        "Implementation Tool".equals(sample.getDescription())
                )
                .expectNextMatches(sample ->
                        "SAMPLE-00006".equals(sample.getSampleId()) &&
                        "Integration Layer".equals(sample.getDescription())
                )
                .expectNextMatches(sample ->
                        "SAMPLE-00005".equals(sample.getSampleId()) &&
                        "Batch Layer".equals(sample.getDescription())
                )
                .expectNextMatches(sample ->
                        "SAMPLE-00004".equals(sample.getSampleId()) &&
                        "Business Layer".equals(sample.getDescription())
                )
                .expectNextMatches(sample ->
                        "SAMPLE-00003".equals(sample.getSampleId()) &&
                        "Presentation Layer".equals(sample.getDescription())
                )
                .expectNextMatches(sample ->
                        "SAMPLE-00002".equals(sample.getSampleId()) &&
                        "Persistence Layer".equals(sample.getDescription())
                )
                .expectNextMatches(sample ->
                        "SAMPLE-00001".equals(sample.getSampleId()) &&
                        "Foundation Layer".equals(sample.getDescription())
                )
                .verifyComplete();
    }
}
