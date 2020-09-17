package finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.mapper;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.api.publisher.model.DateOLHCAV;
import finance.modelling.data.sourcefinancialhistoricaltimeseries.client.dto.DateOHLCAVDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DateOLHCAVMapper {
    DateOLHCAVMapper INSTANCE = Mappers.getMapper(DateOLHCAVMapper.class);

    DateOLHCAV dateOLHCAVDTOToDateOLHCAV(DateOHLCAVDTO dto);

}
