package finance.modelling.data.ingestfinancialhistoricaltimeseries.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TickerTimeSeriesDTO {
    private String symbol;
    private List<DateOHLCAVDTO> timeSeries;
}
