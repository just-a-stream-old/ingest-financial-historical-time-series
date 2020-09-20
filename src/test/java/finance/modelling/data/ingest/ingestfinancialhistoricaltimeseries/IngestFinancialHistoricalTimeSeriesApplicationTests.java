package finance.modelling.data.ingest.ingestfinancialhistoricaltimeseries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class IngestFinancialHistoricalTimeSeriesApplicationTests {

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
