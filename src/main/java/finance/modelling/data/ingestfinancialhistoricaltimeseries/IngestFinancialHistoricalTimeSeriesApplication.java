package finance.modelling.data.ingestfinancialhistoricaltimeseries;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.service.StockHistoricalTimeSeriesService;
import finance.modelling.fmcommons.data.schema.eod.enums.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class IngestFinancialHistoricalTimeSeriesApplication {

	@Autowired StockHistoricalTimeSeriesService stockHistoricalTimeSeriesService;

	// Todo: Add eod client configuration to group together related @Values injected into services
	// Todo: Send certain failures to DLQ or implement a stateful retry schedule
	// Todo: Set up something automated to continuously consume data in real-time? Or seperate service for real-time feed?

	public static void main(String[] args) {
		SpringApplication.run(IngestFinancialHistoricalTimeSeriesApplication.class, args);
	}

	@PostConstruct
	void init() {
		stockHistoricalTimeSeriesService.ingestAllHistoricalStockTimeSeries(Interval.DAY);
	}

}
