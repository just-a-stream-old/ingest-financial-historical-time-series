package finance.modelling.data.ingestfinancialhistoricaltimeseries.publisher;

import finance.modelling.fmcommons.data.logging.LogPublisher;
import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerTimeSeriesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static finance.modelling.fmcommons.data.helper.api.publisher.PublisherHelper.buildProducerRecordWithTraceIdHeader;

@Component
@Slf4j
public class KafkaTimeSeriesPublisherImpl implements KafkaTimeSeriesPublisher {

    private final KafkaTemplate<String, Object> template;

    public KafkaTimeSeriesPublisherImpl(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void publishMessage(String topic, EodTickerTimeSeriesDTO payload) {
        String traceId = UUID.randomUUID().toString();
        template.send(buildProducerRecordWithTraceIdHeader(topic, payload.getSymbol(), payload, traceId));
        LogPublisher.logInfoDataItemSent(EodTickerTimeSeriesDTO.class, topic, traceId);
    }
}
