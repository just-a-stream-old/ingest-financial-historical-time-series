package finance.modelling.data.ingestfinancialhistoricaltimeseries.repository.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {

    private final String envName;

    public ReactiveMongoConfig(@Value("${context.envName}") String envName) {
        this.envName = envName;
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return envName;
    }
}
