package finance.modelling.data.sourcefinancialhistoricaltimeseries.client;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.dto.TickerTimeSeriesDTO;
import reactor.core.publisher.Mono;

import java.net.URI;

public interface EODHistoricalClient {
    Mono<TickerTimeSeriesDTO> getStockHistoricalTimeSeries(URI resourceUri, String ticker);
}
