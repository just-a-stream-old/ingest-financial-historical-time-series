package finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.TickerTimeSeries;

public interface KafkaTimeSeriesPublisher {
    void publishMessage(String topic, TickerTimeSeries message);
}

