package com.example.producer;


import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.Properties;

@Slf4j
@Component
public class ConsumerMain implements Runnable {

    //public IncidentNotificationRecord incidentNotificationRecord;

    public Properties props;
    private String topic;
    Consumer<String, IncidentNotificationRecord> consumer;

    public ConsumerMain() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "myTopic4");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, IncidentNotificationRecord.class.getName());
        this.props = props;
        this.topic = "myTopic4";

        Consumer<String, IncidentNotificationRecord> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        this.consumer = consumer;
    }

    @Override
    public void run() {

        while (true) {
            ConsumerRecords<String, IncidentNotificationRecord> records = consumer.poll(200);
            if (records.count() != 0) {
                for (ConsumerRecord<String, IncidentNotificationRecord> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                    System.out.println("name = " + record.value().getName());
                }

            }
            consumer.commitAsync();

        }
    }
}




