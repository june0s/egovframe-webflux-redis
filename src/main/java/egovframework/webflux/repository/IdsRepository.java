package egovframework.webflux.repository;

import egovframework.webflux.entity.Ids;
import org.egovframe.rte.psl.reactive.redis.repository.EgovRedisRepository;
import org.egovframe.rte.ptl.reactive.annotation.EgovRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EgovRepository
public class IdsRepository extends EgovRedisRepository<Ids> {

    private String redisKey = "ids";

    public IdsRepository(
            @Qualifier("reactiveRedisConnectionFactory") ReactiveRedisConnectionFactory connectionFactory,
            @Qualifier("idsSerializationContext") RedisSerializationContext serializationContext
    ) {
        super(connectionFactory, serializationContext);
    }

    public Flux<Ids> selectAllIds() {
        return selectData(redisKey);
    }

    public Mono<Ids> selectOneIds(String tableName) {
        return selectData(redisKey).filter(value -> value.getTableName().equals(tableName)).last();
    }

    public Mono<Long> findIndex(Ids ids) {
        return findIndex(redisKey, ids);
    }

    public Mono<Long> countIds() {
        return countData(redisKey);
    }

    public Mono<Ids> insertIds(Ids ids) {
        return insertData(redisKey, ids);
    }

    public Mono<Ids> updateIds(Long idx, Ids ids) {
        return updateData(redisKey, idx, ids);
    }

    public Mono<Boolean> deleteAllIds() {
        return deleteAllData(redisKey);
    }

    public Mono<Boolean> deleteIds(Ids ids) {
        return deleteData(redisKey, ids);
    }

}
