package finance.modelling.data.ingestfinancialhistoricaltimeseries.client;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.client.mapper.EODHistoricalMapper;
import finance.modelling.fmcommons.data.helper.client.EodHistoricalClientHelper;
import finance.modelling.fmcommons.data.schema.eod.dto.EodDateOHLCAVDTO;
import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerTimeSeriesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;

@Component
@Slf4j
public class EODHistoricalClientImpl implements EODHistoricalClient {

    private final WebClient client;
    private final EodHistoricalClientHelper eodHelper;

    public EODHistoricalClientImpl(WebClient client, EodHistoricalClientHelper eodHelper) {
        this.client = client;
        this.eodHelper = eodHelper;
    }

    public Mono<EodTickerTimeSeriesDTO> getStockHistoricalTimeSeries(URI resourceUri, String ticker) {
        return client
                .get()
                .uri(resourceUri)
                .retrieve()
                .bodyToFlux(EodDateOHLCAVDTO.class)
                .collectList()
                .map(dataPoint -> EODHistoricalMapper.mapDateOHLCAVDTOListToTickerTimeSeriesDTO(dataPoint, ticker))
                .onErrorMap(eodHelper::returnTechnicalException)
                .retryWhen(getRetry());
    }

    protected Retry getRetry() {
        return Retry
                .backoff(3, Duration.ofMillis(4000000))
                .filter(eodHelper::isRetryableException);
    }
}
