package finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.TickerTimeSeries;
import finance.modelling.fmcommons.logging.LogPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static finance.modelling.fmcommons.helper.api.publisher.PublisherHelper.buildMessageWithTraceIdHeader;

@Component
@Slf4j
public class KafkaTimeSeriesPublisherImpl implements KafkaTimeSeriesPublisher {

    private final KafkaTemplate<String, Object> template;

    public KafkaTimeSeriesPublisherImpl(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void publishMessage(String topic, TickerTimeSeries payload) {
        UUID traceId = UUID.randomUUID();
        template.send(topic, payload.getSymbol(), buildMessageWithTraceIdHeader(payload, traceId));
        LogPublisher.logInfoDataItemSent(TickerTimeSeries.class, topic, List.of(traceId));
    }
}
