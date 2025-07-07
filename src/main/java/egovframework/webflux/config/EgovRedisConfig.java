package egovframework.webflux.config;

import egovframework.webflux.entity.Ids;
import egovframework.webflux.entity.Sample;
import org.egovframe.rte.psl.reactive.redis.connect.EgovRedisConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@Configuration
@PropertySource("classpath:application.yml")
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class EgovRedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.nodes}")
    private String nodes;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean(name="reactiveRedisConnectionFactory")
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        // 1. egov 가이드
//        EgovRedisConfiguration egovRedisConfiguration = new EgovRedisConfiguration(this.host, this.port);
//        return egovRedisConfiguration.reactiveRedisConnectionFactory();
        // 2. standalone 비밀번호 설정
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
//        config.setPassword(RedisPassword.of(password));
        // 3. redis cluster 모드
        System.out.println("nodes = " + nodes);
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(
                Arrays.asList(
                        nodes
                )
        );
        clusterConfiguration.setPassword(password);

        return new LettuceConnectionFactory(clusterConfiguration);
    }

    @Bean(name="idsSerializationContext")
    public RedisSerializationContext<String, Ids> idsReactiveRedisTemplate() {
        Jackson2JsonRedisSerializer<Ids> serializer = new Jackson2JsonRedisSerializer<>(Ids.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Ids> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        return builder.value(serializer).hashKey(serializer).hashValue(serializer).build();
    }

    @Bean(name="sampleSerializationContext")
    public RedisSerializationContext<String, Sample> sampleReactiveRedisTemplate() {
        Jackson2JsonRedisSerializer<Sample> serializer = new Jackson2JsonRedisSerializer<>(Sample.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Sample> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        return builder.value(serializer).hashKey(serializer).hashValue(serializer).build();
    }

}
