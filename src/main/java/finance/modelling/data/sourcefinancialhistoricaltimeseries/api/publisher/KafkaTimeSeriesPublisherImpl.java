package finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.DateOLHCAV;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.TickerTimeSeries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaTimeSeriesPublisherImpl implements KafkaTimeSeriesPublisher {

    private final KafkaTemplate<String, Object> template;

    public KafkaTimeSeriesPublisherImpl(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void publishMessage(String topic, TickerTimeSeries message) {
        template.send(topic, message.getSymbol(), message);
    }

//    public void reactorPublishMessage(String topic, TickerTimeSeries message) {
//        sender.send(Flux<ProducerRecords>);
//    }
}
