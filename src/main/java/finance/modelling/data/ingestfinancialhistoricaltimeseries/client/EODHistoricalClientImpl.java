package finance.modelling.data.ingestfinancialhistoricaltimeseries.client;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.client.dto.DateOHLCAVDTO;
import finance.modelling.data.ingestfinancialhistoricaltimeseries.client.dto.TickerTimeSeriesDTO;
import finance.modelling.data.ingestfinancialhistoricaltimeseries.client.mapper.EODHistoricalMapper;
import finance.modelling.fmcommons.helper.client.EodHistoricalClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
public class EODHistoricalClientImpl implements EODHistoricalClient {

    private final WebClient client;
    private final EodHistoricalClientHelper eodHelper;

    public EODHistoricalClientImpl(WebClient client, EodHistoricalClientHelper eodHelper) {
        this.client = client;
        this.eodHelper = eodHelper;
    }

    public Mono<TickerTimeSeriesDTO> getStockHistoricalTimeSeries(URI resourceUri, String ticker) {
        return client
                .get()
                .uri(resourceUri)
                .retrieve()
                .bodyToFlux(DateOHLCAVDTO.class)
                .onErrorMap(eodHelper::returnTechnicalException)
                .retryWhen(eodHelper.getRetry())
                .collectList()
                .map(dataPoint -> EODHistoricalMapper.mapDateOHLCAVDTOListToTickerTimeSeriesDTO(dataPoint, ticker));
    }
}
