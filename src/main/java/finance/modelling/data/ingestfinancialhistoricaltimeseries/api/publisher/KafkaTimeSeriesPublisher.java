package finance.modelling.data.ingestfinancialhistoricaltimeseries.api.publisher;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.api.publisher.model.TickerTimeSeries;

public interface KafkaTimeSeriesPublisher {
    void publishMessage(String topic, TickerTimeSeries payload);
}

