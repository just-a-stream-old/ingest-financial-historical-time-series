package finance.modelling.data.ingestfinancialhistoricaltimeseries.service;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.repository.model.Ticker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
class StockHistoricalTimeSeriesServiceImplTest {

    @Autowired private StockHistoricalTimeSeriesServiceImpl timeSeriesService;

    @Test
    void ingestAllHistoricalStockTimeSeries() {
    }

    @Test
    void shouldReturnSymbolWithUSExchangeCodeWithSymbolFromUSAProvided() {
        Ticker mockTicker = mock(Ticker.class);

        doReturn("AAPL").when(mockTicker).getSymbol();
        doReturn("USA").when(mockTicker).getCountry();

        String actualReturn = timeSeriesService.appendExchangeCodeToSymbol(mockTicker);

        verify(mockTicker, times(1)).getSymbol();
        verify(mockTicker, times(1)).getCountry();

        assertThat(actualReturn, equalTo("AAPL.US"));
    }

    @Test
    void shouldReturnSymbolWithExchangeCodeAppendedWithNonUSASymbolProvided() {
        Ticker mockTicker = mock(Ticker.class);

        doReturn("AAPL").when(mockTicker).getSymbol();
        doReturn("UK").when(mockTicker).getCountry();
        doReturn("LSE").when(mockTicker).getExchangeCode();

        String actualReturn = timeSeriesService.appendExchangeCodeToSymbol(mockTicker);

        verify(mockTicker, times(1)).getSymbol();
        verify(mockTicker, times(1)).getCountry();
        verify(mockTicker, times(1)).getExchangeCode();

        assertThat(actualReturn, equalTo("AAPL.LSE"));
    }

    @Test
    void ingestHistoricalStockTimeSeries() {
    }

    @Test
    void buildStockTimeSeriesUri() {
    }
}