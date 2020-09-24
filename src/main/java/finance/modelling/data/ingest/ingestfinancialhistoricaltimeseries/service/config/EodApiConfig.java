package finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries.service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Getter
public class EodApiConfig {

    private final String baseUrl;
    private final String apiKey;
    private final Duration requestDelayMs;
    private final String timeSeriesResource;

    public EodApiConfig(
            @Value("${client.eod.baseUrl}") String baseUrl,
            @Value("${client.eod.security.key}") String apiKey,
            @Value("${client.eod.request.delay.ms}") Long requestDelayMs,
            @Value("${client.eod.resource.eodTimeSeries}") String timeSeriesResource) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.requestDelayMs = Duration.ofMillis(requestDelayMs);
        this.timeSeriesResource = timeSeriesResource;
    }
}
