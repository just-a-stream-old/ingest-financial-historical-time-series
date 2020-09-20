package finance.modelling.data.ingestfinancialhistoricaltimeseries.publisher.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private final Integer numPartitions;
    private final Integer numReplicas;
    private final String outputTimeSeriesTopic;

    public KafkaTopicConfig(
            @Value("${kafka.bindings.publisher.partitions}") Integer numPartitions,
            @Value("${kafka.bindings.publisher.replicas}") Integer numReplicas,
            @Value("${kafka.bindings.publisher.eod.eodTimeSeries}") String outputTimeSeriesTopic) {
        this.numPartitions = numPartitions;
        this.numReplicas = numReplicas;
        this.outputTimeSeriesTopic = outputTimeSeriesTopic;
    }

    @Bean
    public NewTopic outputTimeSeriesTopic() {
        return TopicBuilder
                .name(outputTimeSeriesTopic)
                .partitions(numPartitions)
                .replicas(numReplicas)
                .compact()
                .build();
    }
}
