package egovframework.webflux.service.impl;

import egovframework.webflux.entity.Ids;
import egovframework.webflux.entity.Sample;
import egovframework.webflux.repository.IdsRepository;
import egovframework.webflux.repository.SampleRepository;
import egovframework.webflux.service.SampleService;
import egovframework.webflux.service.SampleVO;
import egovframework.webflux.util.EgovAppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.reactive.idgnr.EgovSequenceGenerator;
import org.egovframe.rte.ptl.reactive.annotation.EgovService;
import org.egovframe.rte.ptl.reactive.exception.EgovErrorCode;
import org.egovframe.rte.ptl.reactive.exception.EgovException;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@EgovService
@Slf4j
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

    private final IdsRepository idsRepository;

    private final SampleRepository sampleRepository;

    private int sequence = 1;

    public Flux<SampleVO> list() {
        return this.sampleRepository.selectAllSample().map(EgovAppUtils::sampleEntityToVo);
    }

    public Flux<SampleVO> search(SampleVO sampleVO) {
        if ("0".equals(sampleVO.getSearchCondition())) {
            return this.sampleRepository.selectSearchSampleSampleId(sampleVO.getSearchKeyword() + "*")
                    .map(EgovAppUtils::sampleEntityToVo);
        } else if ("1".equals(sampleVO.getSearchCondition())) {
            return this.sampleRepository.selectSearchSampleName(sampleVO.getSearchKeyword() + "*")
                    .map(EgovAppUtils::sampleEntityToVo);
        } else {
            return this.list();
        }
    }

    public Mono<SampleVO> detail(String id) {
        return this.sampleRepository.selectOneSample(id).map(EgovAppUtils::sampleEntityToVo);
    }

    public Mono<SampleVO> add(SampleVO sampleVO) {
        Sample sample = EgovAppUtils.sampleVoToEntity(sampleVO);

        try {
            sequence = this.idsRepository.selectOneIds("sample")
                    .flatMap(result -> {
                        result.setSeq(ObjectUtils.isEmpty(result.getSeq()) ? 1 : result.getSeq() + 1);
                        return this.idsRepository.updateIds(0L, result);
                    })
                    .then(this.idsRepository.selectOneIds("sample"))
                    .map(Ids::getSeq).toFuture().get();
        } catch (InterruptedException e) {
            return Mono.error(new EgovException(EgovErrorCode.INTERNAL_SERVER_ERROR, "SampleService InterruptedException Occurred"));
        } catch (ExecutionException e) {
            return Mono.error(new EgovException(EgovErrorCode.INTERNAL_SERVER_ERROR, "SampleService ExecutionException Occurred"));
        }

        sample.setId(EgovSequenceGenerator.generateSequence("SHA-1"));
        sample.setSampleId("SAMPLE-".concat(String.format("%05d", sequence)));

        return this.sampleRepository.insertSample(sample).map(EgovAppUtils::sampleEntityToVo);
    }

    public Mono<SampleVO> update(SampleVO sampleVO) {
        Sample sample = EgovAppUtils.sampleVoToEntity(sampleVO);

        return this.sampleRepository.selectOneSample(sample.getId())
                .flatMap(this.sampleRepository::findIndex)
                .flatMap(result -> this.sampleRepository.updateSample(result, sample))
                .map(EgovAppUtils::sampleEntityToVo);
    }

    public Mono<Boolean> delete(String id) {
        return this.sampleRepository.selectOneSample(id)
                .flatMap(this.sampleRepository::deleteSample);
    }

}
