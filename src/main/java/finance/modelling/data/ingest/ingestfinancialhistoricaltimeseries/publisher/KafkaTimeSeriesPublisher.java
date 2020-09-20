package finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.publisher;

import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerTimeSeriesDTO;

public interface KafkaTimeSeriesPublisher {
    void publishMessage(String topic, EodTickerTimeSeriesDTO payload);
}

