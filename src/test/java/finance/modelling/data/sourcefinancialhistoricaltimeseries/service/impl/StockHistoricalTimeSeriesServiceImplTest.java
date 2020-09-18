package finance.modelling.data.sourcefinancialhistoricaltimeseries.service.impl;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.model.Ticker;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.StockHistoricalTimeSeriesServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest
class StockHistoricalTimeSeriesServiceImplTest {

    @Autowired private StockHistoricalTimeSeriesServiceImpl timeSeriesService;

    @Test
    void ingestAllHistoricalStockTimeSeries() {
    }

    @Test
    void shouldAppendUSInsteadOfExchangeCodeToSymbolFromUSA() {
//        Ticker mockTicker = mock(Ticker.class);
//
//        doReturn("AAPL").when(mockTicker).getSymbol();
//        doReturn("USA").when(mockTicker).getCountry();

//        String actualReturn = timeSeriesService.
    }

    @Test
    void ingestHistoricalStockTimeSeries() {
    }

    @Test
    void buildStockTimeSeriesUri() {
    }
}