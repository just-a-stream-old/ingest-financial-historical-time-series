package finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TopicConfig {

    private final String traceIdHeaderName;
    private final String eodTickerTopic;
    private final String eodTimeSeriesTopic;

    public TopicConfig(
            @Value("${spring.kafka.trace.header}") String traceIdHeaderName,
            @Value("${kafka.bindings.publisher.eod.eodTickers}") String eodTickerTopic,
            @Value("${kafka.bindings.publisher.eod.eodTimeSeries}") String eodTimeSeriesTopic) {
        this.traceIdHeaderName = traceIdHeaderName;
        this.eodTickerTopic = eodTickerTopic;
        this.eodTimeSeriesTopic = eodTimeSeriesTopic;
    }
}
