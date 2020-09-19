package finance.modelling.data.ingestfinancialhistoricaltimeseries.client;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.client.dto.TickerTimeSeriesDTO;
import reactor.core.publisher.Mono;

import java.net.URI;

public interface EODHistoricalClient {
    Mono<TickerTimeSeriesDTO> getStockHistoricalTimeSeries(URI resourceUri, String ticker);
}
