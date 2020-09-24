package finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.service;

import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.client.EODHistoricalClient;
import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.publisher.KafkaTimeSeriesPublisher;
import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.api.consumer.KafkaConsumerTickerImpl;
import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.service.config.EodApiConfig;
import finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.service.config.TopicConfig;
import finance.modelling.fmcommons.data.helper.client.EodHistoricalClientHelper;
import finance.modelling.fmcommons.data.logging.LogClient;
import finance.modelling.fmcommons.data.logging.LogConsumer;
import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerDTO;
import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerTimeSeriesDTO;
import finance.modelling.fmcommons.data.schema.eod.enums.Interval;
import finance.modelling.fmcommons.data.schema.fmp.dto.FmpTickerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

import static finance.modelling.fmcommons.data.logging.LogClient.buildResourcePath;
import static finance.modelling.fmcommons.data.logging.LogConsumer.determineTraceIdFromHeaders;

@Service
@Slf4j
public class StockHistoricalTimeSeriesServiceImpl implements StockHistoricalTimeSeriesService {

    private final EodHistoricalClientHelper eodHelper;
    private final KafkaConsumerTickerImpl kafkaConsumer;
    private final TopicConfig topics;
    private final EODHistoricalClient eodHistoricalClient;
    private final EodApiConfig eodApi;
    private final KafkaTimeSeriesPublisher kafkaPublisher;
    private final String logResourcePath;

    public StockHistoricalTimeSeriesServiceImpl(
            EodHistoricalClientHelper eodHelper,
            KafkaConsumerTickerImpl kafkaConsumer,
            TopicConfig topics, EODHistoricalClient eodHistoricalClient,
            EodApiConfig eodApi, KafkaTimeSeriesPublisher kafkaPublisher) {
        this.eodHelper = eodHelper;
        this.kafkaConsumer = kafkaConsumer;
        this.topics = topics;
        this.eodHistoricalClient = eodHistoricalClient;
        this.eodApi = eodApi;
        this.kafkaPublisher = kafkaPublisher;
        this.logResourcePath = buildResourcePath(eodApi.getBaseUrl(), eodApi.getTimeSeriesResource());
    }

    public void ingestAllHistoricalStockTimeSeries(Interval interval) {
        kafkaConsumer
                .receiveMessages(topics.getEodTickerTopic())
                .delayElements(eodApi.getRequestDelayMs())
                .doOnNext(message -> ingestHistoricalStockTimeSeries(message.value().getSymbol(), interval))
                .subscribe(
                        message -> LogConsumer.logInfoDataItemConsumed(FmpTickerDTO.class, topics.getEodTickerTopic(),
                                determineTraceIdFromHeaders(message.headers(), topics.getTraceIdHeaderName())),
                        error -> LogConsumer.logErrorFailedToConsumeDataItem(FmpTickerDTO.class, topics.getEodTickerTopic())
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
                .doOnNext(timeSeries -> kafkaPublisher.publishMessage(topics.getEodTimeSeriesTopic(), timeSeries))
                .subscribe(
                        timeSeries -> LogClient.logInfoDataItemReceived(
                                timeSeries.getSymbol(), EodTickerTimeSeriesDTO.class, logResourcePath),
                        error ->  eodHelper.respondToErrorType(symbol, EodTickerTimeSeriesDTO.class, error, logResourcePath)
                );
    }

    protected URI buildStockTimeSeriesUri(String symbol, Interval interval) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(eodApi.getBaseUrl())
                .path(eodApi.getTimeSeriesResource().concat(symbol))
                .queryParam("period", interval.toString())
                .queryParam("fmt", "json")
                .queryParam("api_token", eodApi.getApiKey())
                .build()
                .toUri();
    }
}