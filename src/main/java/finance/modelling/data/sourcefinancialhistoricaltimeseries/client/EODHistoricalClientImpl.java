package finance.modelling.data.sourcefinancialhistoricaltimeseries.client;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.dto.DateOHLCAVDTO;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.dto.TickerTimeSeriesDTO;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.mapper.EODHistoricalMapper;
import finance.modelling.fmcommons.exception.client.ClientDailyRequestLimitReachedException;
import finance.modelling.fmcommons.exception.client.InvalidApiKeyException;
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

    public EODHistoricalClientImpl(WebClient client) {
        this.client = client;
    }

    public Mono<TickerTimeSeriesDTO> getStockHistoricalTimeSeries(URI resourceUri, String ticker) {
        return client
                .get()
                .uri(resourceUri)
                .retrieve()
                .bodyToFlux(DateOHLCAVDTO.class)
                .onErrorMap(this::returnTechnicalException)
                .retryWhen(getRetry())
                .collectList()
                .map(dataPoint -> EODHistoricalMapper.mapDateOHLCAVDTOListToTickerTimeSeriesDTO(dataPoint, ticker));
    }

    protected Throwable returnTechnicalException(Throwable error) {
        Throwable customException;
        if (is402PaymentRequiredResponse(error)) {
            customException = new ClientDailyRequestLimitReachedException("100k Requests", error);
        }
        else if (is401InvalidAuthorisationResponse(error)) {
            customException = new InvalidApiKeyException("Invalid Api Key. Have you copied is correctly?", error);
        }
        else {
            customException = error;
        }
        return customException;
    }

    protected boolean is402PaymentRequiredResponse(Throwable error) {
        return error.getMessage().contains("402 Payment Required from GET");
    }

    protected boolean is401InvalidAuthorisationResponse(Throwable error) {
        return error.getMessage().contains("401 Unauthorized from GET");
    }



    protected Retry getRetry() {
        return Retry
                .backoff(10, Duration.ofMillis(200))
                .doAfterRetry(something -> log.info(something.toString()))
                .filter(this::isNotRetryableException);
    }

    protected boolean isNotRetryableException(Throwable error) {
        boolean isNotRetryable = false;
        if (
                error.getClass().equals(ClientDailyRequestLimitReachedException.class) ||
                error.getClass().equals(InvalidApiKeyException.class)
        ) {
            isNotRetryable = true;
        }
        return isNotRetryable;
    }

}
