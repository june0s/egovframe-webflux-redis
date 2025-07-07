package egovframework.webflux.repository;

import egovframework.webflux.entity.Sample;
import org.egovframe.rte.psl.reactive.redis.repository.EgovRedisRepository;
import org.egovframe.rte.ptl.reactive.annotation.EgovRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@EgovRepository
public class SampleRepository extends EgovRedisRepository<Sample> {

    private String redisKey = "sample";

    public SampleRepository(
            @Qualifier("reactiveRedisConnectionFactory") ReactiveRedisConnectionFactory connectionFactory,
            @Qualifier("sampleSerializationContext") RedisSerializationContext serializationContext
    ) {
        super(connectionFactory, serializationContext);
    }

    public Flux<Sample> selectAllSample() {
        return selectData(redisKey);
    }

    public Flux<Sample> selectSearchSampleSampleId(String keyword) {
        Pattern regex = Pattern.compile(keyword);
        return this.selectAllSample().filter(value -> regex.matcher(value.getSampleId()).find());
    }

    public Flux<Sample> selectSearchSampleName(String keyword) {
        Pattern regex = Pattern.compile(keyword);
        return this.selectAllSample().filter(value -> regex.matcher(value.getName()).find());
    }

    public Mono<Sample> selectOneSample(String id) {
        return selectData(redisKey).filter(value -> value.getId().equals(id)).last();
    }

    public Mono<Long> findIndex(Sample sample) {
        return findIndex(redisKey, sample);
    }

    public Mono<Sample> insertSample(Sample sample) {
        return insertData(redisKey, sample);
    }

    public Mono<Sample> updateSample(Long idx, Sample sample) {
        return updateData(redisKey, idx, sample);
    }

    public Mono<Boolean> deleteAllSample() {
        return deleteAllData(redisKey);
    }

    public Mono<Boolean> deleteSample(Sample sample) {
        return deleteData(redisKey, sample).hasElement();
    }

}
