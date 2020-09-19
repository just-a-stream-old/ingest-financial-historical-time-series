package finance.modelling.data.ingestfinancialhistoricaltimeseries.service;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.service.enums.Interval;

public interface StockHistoricalTimeSeriesService {
    void ingestAllHistoricalStockTimeSeries(Interval interval);
    void ingestHistoricalStockTimeSeries(String ticker, Interval interval);
}
