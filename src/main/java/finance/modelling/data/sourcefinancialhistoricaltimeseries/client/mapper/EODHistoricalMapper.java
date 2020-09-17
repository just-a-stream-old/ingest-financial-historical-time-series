package finance.modelling.data.sourcefinancialhistoricaltimeseries.client.mapper;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.dto.DateOHLCAVDTO;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.dto.TickerTimeSeriesDTO;

import java.util.List;

public class EODHistoricalMapper {

    public static TickerTimeSeriesDTO mapDateOHLCAVDTOListToTickerTimeSeriesDTO(
            List<DateOHLCAVDTO> dateOHLCAVDTOList,
            String ticker
    ) {
        return TickerTimeSeriesDTO
                .builder()
                .symbol(ticker)
                .timeSeries(dateOHLCAVDTOList)
                .build();
    }
}

