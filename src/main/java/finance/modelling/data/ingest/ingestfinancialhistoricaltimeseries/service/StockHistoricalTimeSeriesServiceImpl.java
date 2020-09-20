package finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.service;

import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.client.EODHistoricalClient;
import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.publisher.KafkaTimeSeriesPublisher;
import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.api.consumer.KafkaConsumerTickerImpl;
import finance.modelling.fmcommons.data.helper.client.EodHistoricalClientHelper;
import finance.modelling.fmcommons.data.logging.LogClient;
import finance.modelling.fmcommons.data.logging.LogConsumer;
import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerDTO;
import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerTimeSeriesDTO;
import finance.modelling.fmcommons.data.schema.eod.enums.Interval;
import finance.modelling.fmcommons.data.schema.fmp.dto.FmpTickerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;

import static finance.modelling.fmcommons.data.logging.LogClient.buildResourcePath;
import static finance.modelling.fmcommons.data.logging.LogConsumer.determineTraceIdFromHeaders;

@Service
@Slf4j
public class StockHistoricalTimeSeriesServiceImpl implements StockHistoricalTimeSeriesService {

    private final EodHistoricalClientHelper eodHelper;
    private final KafkaConsumerTickerImpl kafkaConsumer;
    private final String inputTickerTopic;
    private final EODHistoricalClient eodHistoricalClient;
    private final KafkaTimeSeriesPublisher kafkaPublisher;
    private final String outputTimeSeriesTopic;
    private final String eodApiKey;
    private final String eodBaseUrl;
    private final String timeSeriesResourceUrl;
    private final String logResourcePath;
    private final Long requestDelayMs;

    public StockHistoricalTimeSeriesServiceImpl(
            EodHistoricalClientHelper eodHelper,
            KafkaConsumerTickerImpl kafkaConsumer,
            @Value("${kafka.bindings.consumer.fmp.fmpTickers}") String inputTickerTopic,
            EODHistoricalClient eodHistoricalClient,
            KafkaTimeSeriesPublisher kafkaPublisher,
            @Value("${kafka.bindings.publisher.eod.eodTimeSeries}") String outputTimeSeriesTopic,
            @Value("${client.eod.security.key}") String eodApiKey,
            @Value("${client.eod.baseUrl}") String eodBaseUrl,
            @Value("${client.eod.resource.eodTimeSeries}") String timeSeriesResourceUrl,
            @Value("${client.eod.request.delay.ms}") Long requestDelayMs) {
        this.eodHelper = eodHelper;
        this.kafkaConsumer = kafkaConsumer;
        this.inputTickerTopic = inputTickerTopic;
        this.eodHistoricalClient = eodHistoricalClient;
        this.kafkaPublisher = kafkaPublisher;
        this.outputTimeSeriesTopic = outputTimeSeriesTopic;
        this.eodApiKey = eodApiKey;
        this.eodBaseUrl = eodBaseUrl;
        this.timeSeriesResourceUrl = timeSeriesResourceUrl;
        this.logResourcePath = buildResourcePath(eodBaseUrl, timeSeriesResourceUrl);
        this.requestDelayMs = requestDelayMs;
    }

    public void ingestAllHistoricalStockTimeSeries(Interval interval) {
        kafkaConsumer
                .receiveMessages(inputTickerTopic)
                .delayElements(Duration.ofMillis(requestDelayMs))
                .doOnNext(message -> ingestHistoricalStockTimeSeries(message.value().getSymbol(), interval))
                .subscribe(
                        message -> LogConsumer.logInfoDataItemConsumed(
                                FmpTickerDTO.class, inputTickerTopic, determineTraceIdFromHeaders(message.headers())),
                        error -> LogConsumer.logErrorFailedToConsumeDataItem(FmpTickerDTO.class, inputTickerTopic)
                );
    }

    public String appendExchangeCodeToSymbol(EodTickerDTO ticker) {
        String symbolWithExchangeCode = ticker.getSymbol().concat(".");
        if (ticker.getCountry().equals("USA")) {
            symbolWithExchangeCode = symbolWithExchangeCode.concat("US");
        }
        else {
            symbolWithExchangeCode = symbolWithExchangeCode.concat(ticker.getExchange());
        }
        return symbolWithExchangeCode;
    }

    public void ingestHistoricalStockTimeSeries(String symbol, Interval interval) {
        eodHistoricalClient
                .getStockHistoricalTimeSeries(buildStockTimeSeriesUri(symbol, interval), symbol)
                .doOnNext(timeSeries -> kafkaPublisher.publishMessage(outputTimeSeriesTopic, timeSeries))
                .subscribe(
                        timeSeries -> LogClient.logInfoDataItemReceived(
                                timeSeries.getSymbol(), EodTickerTimeSeriesDTO.class, logResourcePath),
                        error ->  eodHelper.respondToErrorType(symbol, EodTickerTimeSeriesDTO.class, error, logResourcePath)
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
}
