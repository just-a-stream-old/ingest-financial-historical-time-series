package finance.modelling.data.sourcefinancialhistoricaltimeseries.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DateOHLCAVDTO {
    @JsonProperty("date") private String timestamp;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    @JsonProperty("adjusted_close") private Double adjustedClose;
    private Long volume;
}
