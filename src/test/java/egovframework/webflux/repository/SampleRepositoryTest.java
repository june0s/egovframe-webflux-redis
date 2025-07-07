package egovframework.webflux.repository;

import egovframework.webflux.entity.Sample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SampleRepositoryTest {

    @Autowired
    private SampleRepository sampleRepository;

    /*
[
  {
    "searchCondition": "",
    "searchKeyword": "",
    "id": "4edbafb9bca25c4314662c002c79c647a5c04098",
    "sampleId": "SAMPLE-00008",
    "name": "Development Environment",
    "description": "Test Tool",
    "useYn": "Y",
    "regUser": "eGov"
  },
  {
    "searchCondition": "",
    "searchKeyword": "",
    "id": "fc513012d0a91a1493846108f3086f761e35d171",
    "sampleId": "SAMPLE-00007",
    "name": "Development Environment",
    "description": "Implementation Tool",
    "useYn": "Y",
    "regUser": "eGov"
  }
]

    * */
    @Test
    public void count() throws Exception {
        Flux<Sample> result = sampleRepository.selectAllSample();


        StepVerifier.create(result)
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void content() throws Exception {
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
