package finance.modelling.data.sourcefinancialhistoricaltimeseries.service.contract;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums.Interval;

public interface StockHistoricalTimeSeriesService {
    void ingestAllHistoricalStockTimeSeries(Interval interval);
    void ingestHistoricalStockTimeSeries(String ticker, Interval interval);
}
