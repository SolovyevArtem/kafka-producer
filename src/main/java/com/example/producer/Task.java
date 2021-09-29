package com.example.producer;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Data
@Log4j2
public class Task {
    private  int id;
    private  String state;
    private  String worker_id;
    private  String trace;

    public void restart(String connectorUrl) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json");
        //httpHeaders.add("Accept","application/vnd.kafka.json.v2+json");
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<JsonNode> configConnectors=  restTemplate.exchange(connectorUrl + "tasks/" + id + "/restart", HttpMethod.POST,entity, JsonNode.class);
        if (configConnectors.getStatusCode().is2xxSuccessful()) {
        log.info("Connector:{} , Task {} restart successful",connectorUrl , id);
        }
        else {
        log.info("Connector:{}  ,  Task: {} restart failed. Status {}",connectorUrl, id, configConnectors.getStatusCode());
        }
    }
}

