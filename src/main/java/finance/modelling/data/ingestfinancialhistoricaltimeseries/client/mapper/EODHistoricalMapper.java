package finance.modelling.data.ingestfinancialhistoricaltimeseries.client.mapper;

import finance.modelling.fmcommons.data.schema.eod.dto.EodDateOHLCAVDTO;
import finance.modelling.fmcommons.data.schema.eod.dto.EodTickerTimeSeriesDTO;

import java.util.List;

public class EODHistoricalMapper {

    public static EodTickerTimeSeriesDTO mapDateOHLCAVDTOListToTickerTimeSeriesDTO(
            List<EodDateOHLCAVDTO> dateOHLCAVDTOList,
            String ticker
    ) {
        return EodTickerTimeSeriesDTO
                .builder()
                .symbol(ticker)
                .timeSeries(dateOHLCAVDTOList)
                .build();
    }
}

