package com.example.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
@Configuration
@EnableScheduling
public class ProducerService {



    public void produce(IncidentNotificationRecord incidentServiceEvent) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer");
        props.put("schema.registry.url", "http://127.0.0.1:8081");
        Producer<String, IncidentNotificationRecord> producer = new KafkaProducer<>(props);
        String topic = "myTopic4";
        String key = "testkey";
        System.out.println( incidentServiceEvent.getTime());
        ProducerRecord<String, IncidentNotificationRecord> record
                = new ProducerRecord<>(topic, key, incidentServiceEvent);
        producer.send(record).get();
        producer.flush();
        producer.close();

    }

    public void produceTask(TaskCommand task) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer");
        props.put("schema.registry.url", "http://127.0.0.1:8081");
        Producer<String, TaskCommand> producer = new KafkaProducer<>(props);
        String topic = "input";
        String key = "testkey";
        System.out.println( task.getTaskUuid());
        ProducerRecord<String, TaskCommand> record
                = new ProducerRecord<>(topic, key, task);
        producer.send(record).get();
        producer.flush();
        producer.close();

    }


    //список урлов коннекторов
    private List<String> urls = new ArrayList(Arrays.asList("http://localhost:8083/connectors/"));

    //списко имен коннекторов
    private List<String> namesConnectors  = new ArrayList<>();



    //мапа урл коннекторов,коннекторы
    private Map<String,List<String>> urlsAndNames = new HashMap<>();

    //списко всех коннекторов
    private List<Connector> connectors  = new ArrayList<>();

    public void restart()  {
        Map<String,List<String>> urlsAndNames = new HashMap<>();
        List<String> namesConnectors  = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = new HttpEntity(new HttpHeaders());
        for (String url : urls) {
            ResponseEntity<String[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, String[].class);
            namesConnectors.addAll(Arrays.asList(response.getBody()));
            urlsAndNames.put(url,namesConnectors);
        }

        this.urlsAndNames = urlsAndNames;

    }

    @Scheduled(fixedDelay =  10000)
    public void scheduleFixedRateTask() {
        try {
            connectors =  initConnectors(urlsAndNames);
            for (Connector connector : connectors){
                if(connector.getState().equals("RUNNING")) {
                   for (Task task: connector.getTasks()){
                      if(!task.getState().equals("RUNNING")) {
                          task.restart(connector.getUrl());
                      }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private List<Connector> initConnectors(Map<String,List<String>> urlsAndNames) throws JsonProcessingException {
        List<Connector> connectors = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = new HttpEntity(new HttpHeaders());
        for (String url: urlsAndNames.keySet()) {
            for (String name: namesConnectors){
                ResponseEntity<JsonNode> configConnectors=  restTemplate.exchange(url + name + "/status", HttpMethod.GET,entity, JsonNode.class);
                int size =  configConnectors.getBody().get("tasks").size();
                ObjectMapper objectMapper = new ObjectMapper();
                String o = configConnectors.getBody().get("tasks").toString();
                List<Task>  tasks = objectMapper.readValue(o,new TypeReference<ArrayList<Task>>(){});
                JsonNode con  =  configConnectors.getBody().get("connector");
                var state = con.get("state");
                Connector connector = new Connector(name,state.textValue(),size,tasks,url + name + "/");
                connectors.add(connector);
                log.info("Time: {}, {} ", ZonedDateTime.now(), connectors.toString());
                return connectors;
            }
        }
        return connectors;

    }
}


