package egovframework.webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SampleService {

    Flux<SampleVO> list();

    Flux<SampleVO> search(SampleVO sampleVO);

    Mono<SampleVO> detail(String id);

    Mono<SampleVO> add(SampleVO sampleVO);

    Mono<SampleVO> update(SampleVO sampleVO);

    Mono<Boolean> delete(String id);

}
