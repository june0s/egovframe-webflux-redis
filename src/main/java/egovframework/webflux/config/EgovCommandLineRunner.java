package egovframework.webflux.config;

import egovframework.webflux.entity.Ids;
import egovframework.webflux.entity.Sample;
import egovframework.webflux.repository.IdsRepository;
import egovframework.webflux.repository.SampleRepository;
import org.egovframe.rte.fdl.reactive.idgnr.EgovSequenceGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class EgovCommandLineRunner implements CommandLineRunner {

    private final IdsRepository idsRepository;
    private final SampleRepository sampleRepository;

    public EgovCommandLineRunner(IdsRepository idsRepository, SampleRepository sampleRepository) {
        this.idsRepository = idsRepository;
        this.sampleRepository = sampleRepository;
    }

    @Override
    public void run(final String... args) {
        Flux<Ids> idsData = Flux.just(
                new Ids(EgovSequenceGenerator.generateSequence("SHA-1"), "sample",8)
        ).log();
        idsRepository.deleteAllIds().thenMany(idsData.flatMap(idsRepository::insertIds).log()).subscribe();

        Flux<Sample> sampleData = Flux.just(
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00001","Runtime Environment","Foundation Layer","Y","eGov"),
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00002","Runtime Environment","Persistence Layer","Y","eGov"),
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00003","Runtime Environment","Presentation Layer","Y","eGov"),
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00004","Runtime Environment","Business Layer","Y","eGov"),
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00005","Runtime Environment","Batch Layer","Y","eGov"),
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00006","Runtime Environment","Integration Layer","Y","eGov"),
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00007","Development Environment","Implementation Tool","Y","eGov"),
                new Sample(EgovSequenceGenerator.generateSequence("SHA-1"), "SAMPLE-00008","Development Environment","Test Tool","Y","eGov")
        ).log();
        sampleRepository.deleteAllSample().thenMany(sampleData.flatMap(sampleRepository::insertSample).log()).subscribe();
    }

}
