package finance.modelling.data.sourcefinancialhistoricaltimeseries.repository;

import finance.modelling.data.sourcefinancialhistoricaltimeseries.repository.model.Ticker;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerRepository extends ReactiveMongoRepository<Ticker, String> {
}