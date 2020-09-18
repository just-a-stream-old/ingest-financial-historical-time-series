package finance.modelling.data.sourcefinancialhistoricaltimeseries;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums.Interval;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.StockHistoricalTimeSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SourceFinancialHistoricalTimeSeriesApplication {

	@Autowired StockHistoricalTimeSeriesService stockHistoricalTimeSeriesService;

	public static void main(String[] args) {
		SpringApplication.run(SourceFinancialHistoricalTimeSeriesApplication.class, args);
	}

	@PostConstruct
	void init() {
		stockHistoricalTimeSeriesService.ingestAllHistoricalStockTimeSeries(Interval.DAY);
	}

}
