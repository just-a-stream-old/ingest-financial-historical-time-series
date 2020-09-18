package finance.modelling.data.sourcefinancialhistoricaltimeseries.service.impl;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.contract.StockHistoricalTimeSeriesService;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums.Interval;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.net.URI;

@SpringBootTest
class StockHistoricalTimeSeriesServiceImplTest {

    @Autowired private StockHistoricalTimeSeriesServiceImpl service;

    @Test
    void ingestAllHistoricalStockTimeSeries() {
    }

    @Test
    void appendExchangeCodeToSymbol() {
    }

    @Test
    void ingestHistoricalStockTimeSeries() {
    }

    @Test
    void buildStockTimeSeriesUri() {
    }

    @Test
    void testURIToString() {

        String test = service.buildStockTimeSeriesUri("AAPL", Interval.DAY).getRawPath();
        System.out.println(test);
    }
}