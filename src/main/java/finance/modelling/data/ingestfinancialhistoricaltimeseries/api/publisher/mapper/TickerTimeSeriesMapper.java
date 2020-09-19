package finance.modelling.data.ingestfinancialhistoricaltimeseries.api.publisher.mapper;

import finance.modelling.data.ingestfinancialhistoricaltimeseries.api.publisher.model.TickerTimeSeries;
import finance.modelling.data.ingestfinancialhistoricaltimeseries.client.dto.TickerTimeSeriesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TickerTimeSeriesMapper {
    TickerTimeSeriesMapper INSTANCE = Mappers.getMapper(TickerTimeSeriesMapper.class);

    TickerTimeSeries tickerTimeSeriesDTOToTickerTimeSeries(TickerTimeSeriesDTO dto);
}

