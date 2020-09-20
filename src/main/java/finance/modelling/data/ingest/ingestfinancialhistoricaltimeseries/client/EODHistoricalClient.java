package finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.client;


import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerTimeSeriesDTO;
import reactor.core.publisher.Mono;

import java.net.URI;

public interface EODHistoricalClient {
    Mono<EodTickerTimeSeriesDTO> getStockHistoricalTimeSeries(URI resourceUri, String ticker);
}
