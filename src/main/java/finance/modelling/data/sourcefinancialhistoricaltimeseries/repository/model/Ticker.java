package finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickers")
@Data
public class Ticker {
    private String symbol;
    private String name;
    private String exchangeCode;
    private String country;
    private String currency;
    private String type;
}
