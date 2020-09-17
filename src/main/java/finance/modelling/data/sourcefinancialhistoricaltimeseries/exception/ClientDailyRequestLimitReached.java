package finance.modelling.data.sourcefinancialhistoricaltimeseries.exception;

public class ClientDailyRequestLimitReached extends Exception {
    public ClientDailyRequestLimitReached(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
