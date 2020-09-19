package finance.modelling.data.ingestfinancialhistoricaltimeseries.api.publisher.model;

import lombok.Data;

import java.util.List;

@Data
public class TickerTimeSeries {
    private String symbol;
    private List<DateOLHCAV> timeSeries;
}
