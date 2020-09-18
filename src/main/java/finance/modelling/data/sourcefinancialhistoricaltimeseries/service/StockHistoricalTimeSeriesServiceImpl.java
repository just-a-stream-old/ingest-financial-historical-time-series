package finance.modelling.data.sourcefinancialhistoricaltimeseries.service;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.KafkaTimeSeriesPublisher;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.mapper.TickerTimeSeriesMapper;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.TickerTimeSeries;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.EODHistoricalClient;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.TickerRepository;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.model.Ticker;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums.Interval;
import finance.modelling.fmcommons.logging.LogClient;
import finance.modelling.fmcommons.logging.LogDb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static finance.modelling.fmcommons.exception.ExceptionParser.*;
import static finance.modelling.fmcommons.logging.LogClient.buildResourcePath;
import static finance.modelling.fmcommons.logging.LogDb.buildDbUri;

@Service
@Slf4j
public class StockHistoricalTimeSeriesServiceImpl implements StockHistoricalTimeSeriesService {

    private final EODHistoricalClient eodHistoricalClient;
    private final TickerRepository tickerRepository;
    private final KafkaTimeSeriesPublisher kafkaPublisher;
    private final String eodApiKey;
    private final String eodBaseUrl;
    private final String timeSeriesResourceUrl;
    private final String logResourcePath;
    private final Long requestDelayMs;
    private final String logDbUri;

    public StockHistoricalTimeSeriesServiceImpl(
            EODHistoricalClient eodHistoricalClient,
            TickerRepository tickerRepository,
            KafkaTimeSeriesPublisher kafkaPublisher,
            @Value("${client.api.eod.security.key}") String eodApiKey,
            @Value("${client.api.eod.baseUrl}") String eodBaseUrl,
            @Value("${client.api.eod.resource.eodTimeSeries}") String timeSeriesResourceUrl,
            @Value("${client.api.request.delay.ms}") Long requestDelayMs,
            @Value("${spring.data.mongodb.host}") String dbHost,
            @Value("${spring.data.mongodb.port}") String dbPort) {
        this.eodHistoricalClient = eodHistoricalClient;
        this.tickerRepository = tickerRepository;
        this.kafkaPublisher = kafkaPublisher;
        this.eodApiKey = eodApiKey;
        this.eodBaseUrl = eodBaseUrl;
        this.timeSeriesResourceUrl = timeSeriesResourceUrl;
        this.logResourcePath = buildResourcePath(eodBaseUrl, timeSeriesResourceUrl);
        this.requestDelayMs = requestDelayMs;
        this.logDbUri = buildDbUri(dbHost, dbPort);
    }

    public void ingestAllHistoricalStockTimeSeries(Interval interval) {
        tickerRepository
                .findAll()
                .map(this::appendExchangeCodeToSymbol)
                .delayElements(Duration.ofMillis(requestDelayMs))
                .doOnNext(ticker -> ingestHistoricalStockTimeSeries(ticker, interval))
                .subscribe(
                        symbol -> LogDb.logDebugDataItemQueried(Ticker.class, symbol, logDbUri),
                        error -> LogDb.logErrorFailedDataItemQuery(Ticker.class, error, logDbUri, List.of("Don't catch exception"))
                );
    }

    public String appendExchangeCodeToSymbol(Ticker ticker) {
        String symbolWithExchangeCode = ticker.getSymbol().concat(".");
        if (ticker.getCountry().equals("USA")) {
            symbolWithExchangeCode = symbolWithExchangeCode.concat("US");
        }
        else {
            symbolWithExchangeCode = symbolWithExchangeCode.concat(ticker.getExchangeCode());
        }
        return symbolWithExchangeCode;
    }

    public void ingestHistoricalStockTimeSeries(String symbol, Interval interval) {
        eodHistoricalClient
                .getStockHistoricalTimeSeries(buildStockTimeSeriesUri(symbol, interval), symbol)
                .map(TickerTimeSeriesMapper.INSTANCE::tickerTimeSeriesDTOToTickerTimeSeries)
                .doOnNext(timeSeries -> kafkaPublisher.publishMessage("example-topic", timeSeries))
                .subscribe(
                        data -> LogClient.logInfoDataItemReceived(
                                symbol, TickerTimeSeries.class, logResourcePath, Map.of("interval", interval)),
                        error -> respondToErrorType(symbol, interval, error)
                );
    }

    protected URI buildStockTimeSeriesUri(String symbol, Interval interval) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(eodBaseUrl)
                .path(timeSeriesResourceUrl.concat(symbol))
                .queryParam("period", interval.toString())
                .queryParam("fmt", "json")
                .queryParam("api_token", eodApiKey)
                .build()
                .toUri();
    }

    protected void respondToErrorType(String symbol, Interval interval, Throwable error) {
        List<String> responsesToError = new LinkedList<>();

        if (isClientDailyRequestLimitReached(error)) {
            // Todo: Implement stateful retry system when max requests limit reached
            responsesToError.add("Scheduled retry...");
        }
        else if (isKafkaException(error)) {
            responsesToError.add("Print stacktrace");
            error.printStackTrace();
        }
        else if (isSaslAuthentificationException(error)) {
            responsesToError.add("Print error message");
            log.error(error.getMessage());
        }
        else {
            responsesToError.add("Default");
        }

        LogClient.logErrorFailedToReceiveDataItem(
                symbol, TickerTimeSeries.class, error, logResourcePath, responsesToError, Map.of("interval", interval));
    }
}
