package finance.modelling.data.sourcefinancialhistoricaltimeseries.service;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums.Interval;

public interface StockHistoricalTimeSeriesService {
    void ingestAllHistoricalStockTimeSeries(Interval interval);
    void ingestHistoricalStockTimeSeries(String ticker, Interval interval);
}
