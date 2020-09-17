package finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model;

import lombok.Data;

import java.util.List;

@Data
public class TickerTimeSeries {
    private String symbol;
    private List<DateOLHCAV> timeSeries;
}
