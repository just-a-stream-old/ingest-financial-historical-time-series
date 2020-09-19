package finance.modelling.data.ingestfinancialhistoricaltimeseries;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.service.enums.Interval;
import finance.modelling.data.ingestfinancialhistoricaltimeseries.service.StockHistoricalTimeSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class IngestFinancialHistoricalTimeSeriesApplication {

	@Autowired StockHistoricalTimeSeriesService stockHistoricalTimeSeriesService;

	public static void main(String[] args) {
		SpringApplication.run(IngestFinancialHistoricalTimeSeriesApplication.class, args);
	}

	@PostConstruct
	void init() {
//		stockHistoricalTimeSeriesService.ingestAllHistoricalStockTimeSeries(Interval.DAY);
		stockHistoricalTimeSeriesService.ingestHistoricalStockTimeSeries("AAPL.US", Interval.DAY);
	}

}
