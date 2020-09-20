package finance.modelling.data.ingestfinancialhistoricaltimeseries.service;


import finance.modelling.fmcommons.data.schema.eod.enums.Interval;

public interface StockHistoricalTimeSeriesService {
    void ingestAllHistoricalStockTimeSeries(Interval interval);
    void ingestHistoricalStockTimeSeries(String ticker, Interval interval);
}
