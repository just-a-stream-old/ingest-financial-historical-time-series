package finance.modelling.data.sourcefinancialhistoricaltimeseries;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.TickerRepository;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.model.Ticker;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.contract.StockHistoricalTimeSeriesService;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums.Interval;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.time.Duration;

@SpringBootTest
@Slf4j
class SourceFinancialHistoricalTimeSeriesApplicationTests {

//	@Autowired
//	StockHistoricalTimeSeriesService stockHistoricalTimeSeriesService;
//
//	@Autowired
//	TickerRepository tickerRepository;
//
//	@Autowired
//	TickerTimeSeriesRepository timeSeriesRepository;
//
//	@Value("${eod.api.request.delay.ms}") Long requestDelayMs;
//
//	@Test
//	void contextLoads() {
//	}
//
//	@Test
//	void getDataImMissing() {
//		tickerRepository
//				.findAll(Sort.by(Sort.Direction.DESC, "_id"))
//				.map(this::appendExchangeCodeToSymbol)
//				.delayElements(Duration.ofMillis(requestDelayMs))
//				.doOnNext(ticker -> stockHistoricalTimeSeriesService.ingestHistoricalStockTimeSeries(ticker, Interval.DAY))
//				.subscribe();
//	}
//
//	protected String appendExchangeCodeToSymbol(Ticker ticker) {
//		String symbolWithExchangeCode = ticker.getSymbol().concat(".");
//		if (ticker.getCountry().equals("USA")) {
//			symbolWithExchangeCode = symbolWithExchangeCode.concat("US");
//		}
//		else {
//			symbolWithExchangeCode = symbolWithExchangeCode.concat(ticker.getExchangeCode());
//		}
//		return symbolWithExchangeCode;
//	}

}