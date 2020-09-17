package finance.modelling.data.sourcefinancialhistoricaltimeseries.service.impl;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.KafkaTimeSeriesPublisher;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.mapper.DateOLHCAVMapper;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.mapper.TickerTimeSeriesMapper;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.DateOLHCAV;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.TickerTimeSeries;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.EODHistoricalClient;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.exception.ClientDailyRequestLimitReached;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.TickerRepository;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.model.Ticker;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums.Interval;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.contract.StockHistoricalTimeSeriesService;
import finance.modelling.fmcommons.logging.LogIngest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StockHistoricalTimeSeriesServiceImpl implements StockHistoricalTimeSeriesService {

    private final EODHistoricalClient eodHistoricalClient;
    private final TickerRepository tickerRepository;
    private final KafkaTimeSeriesPublisher kafkaPublisher;
    private final String eodApiKey;
    private final String eodBaseUrl;
    private final String timeSeriesResourceUrl;
    private final Long requestDelayMs;

    public StockHistoricalTimeSeriesServiceImpl(
            EODHistoricalClient eodHistoricalClient,
            TickerRepository tickerRepository,
            KafkaTimeSeriesPublisher kafkaPublisher,
            @Value("${client.api.eod.security.key}") String eodApiKey,
            @Value("${client.api.eod.baseUrl}") String eodBaseUrl,
            @Value("${client.api.eod.resource.eodTimeSeries}") String timeSeriesResourceUrl,
            @Value("${client.api.request.delay.ms}") Long requestDelayMs) {
        this.eodHistoricalClient = eodHistoricalClient;
        this.tickerRepository = tickerRepository;
        this.kafkaPublisher = kafkaPublisher;
        this.eodApiKey = eodApiKey;
        this.eodBaseUrl = eodBaseUrl;
        this.timeSeriesResourceUrl = timeSeriesResourceUrl;
        this.requestDelayMs = requestDelayMs;
    }

    // Todo: Change logging so it's seperated between 'client ingest' and 'publisher sent'

    public void ingestAllHistoricalStockTimeSeries(Interval interval) {
        tickerRepository
                .findAll()
                .map(this::appendExchangeCodeToSymbol)
                .delayElements(Duration.ofMillis(requestDelayMs))
                .doOnNext(ticker -> ingestHistoricalStockTimeSeries(ticker, interval))
                .subscribe(
                        ticker -> log.debug("Successfully queried ticker from database: {}", ticker),
                        error -> log.warn(String.format("Error occurred whilst querying tickers: %s", error))
                );
    }

    protected String appendExchangeCodeToSymbol(Ticker ticker) {
        String symbolWithExchangeCode = ticker.getSymbol().concat(".");
        if (ticker.getCountry().equals("USA")) {
            symbolWithExchangeCode = symbolWithExchangeCode.concat("US");
        }
        else {
            symbolWithExchangeCode = symbolWithExchangeCode.concat(ticker.getExchangeCode());
        }
        return symbolWithExchangeCode;
    }

    public void ingestHistoricalStockTimeSeries(String ticker, Interval interval) {
        eodHistoricalClient
                .getStockHistoricalTimeSeries(buildStockTimeSeriesUri(ticker, interval), ticker)
                .map(TickerTimeSeriesMapper.INSTANCE::tickerTimeSeriesDTOToTickerTimeSeries)
                .doOnNext(timeSeries -> kafkaPublisher.publishMessage("example-topic", timeSeries))
                .subscribe(
                        data -> LogIngest.logInfoDataItemIngested(
                                ticker, TickerTimeSeries.class, timeSeriesResourceUrl, Map.of("interval", interval)),
                        error -> respondToErrorType(ticker, interval, error)
                );
    }

    protected URI buildStockTimeSeriesUri(String ticker, Interval interval) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(eodBaseUrl)
                .path(timeSeriesResourceUrl.concat(ticker))
                .queryParam("api_token", eodApiKey)
                .queryParam("period", interval.toString())
                .queryParam("fmt", "json")
                .build()
                .toUri();
    }

    protected void respondToErrorType(
            String ticker,
            Interval interval,
            Throwable error) {

        String responseToError;
        if (clientDailyRequestLimitHasBeenReached(error)) {
            // Schedule job w/ centralised-scheduler-microservice
            responseToError = "Scheduled retry...";
        }
        else {
            responseToError = "LogIngest and forget.";
        }
        LogIngest.logErrorFailedDataItemIngestion(
                ticker, TickerTimeSeries.class, error, timeSeriesResourceUrl, List.of(responseToError),
                Map.of("interval", interval));
    }

    protected boolean clientDailyRequestLimitHasBeenReached(Throwable error) {
        return error.getClass().equals(ClientDailyRequestLimitReached.class);
    }
}
