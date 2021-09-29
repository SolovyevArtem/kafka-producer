package com.example.producer;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.concurrent.ExecutionException;

@RestController
public class HomeController {
    @Autowired
    private ProducerService producerService;
    @Autowired
    public ConsumerMain consumerMain;

    @GetMapping("/test")
    public String test (@RequestParam String id,@RequestParam String status) throws ExecutionException, InterruptedException {
       // producerService.produce( new IncidentNotificationRecord(id,"da2fef24-f509-11eb-9a03-0242ac130002" ,status,"name","description",3455f,444f,"address","da2fef24-f509-11eb-9a03-0242ac130003","24","{4}",true,"da2fef24-f509-11eb-9a03-0242ac130002","userRole","00000000-0000-0000-0000-0f93131b988f"));
        producerService.produceTask( new TaskCommand("bf8e3f53-dddf-4131-b33f-7627f83370b6",id, "2c6c0ebd-4be8-439f-9740-0f93131b988f", "notification-service","Start"));
        return "OK";
    }
    @GetMapping("/start")
    public String start () {
        new Thread(consumerMain).start();
        return "Start consumer";
    }
    @GetMapping("/restart")
    public String restart () {
        producerService.restart();
        return "restart connector";
    }
}

// "uuid": "da2fef24-f509-21eb-9a03-0242ac130007", "incident_type_uuid": "da2fef24-f509-11eb-9a03-0242ac130002", "time": "2021-07-29 11:05:19.149000 +00:00", "status": "test", "name": "string", "description": "test", "latitude": 22123, "longitude": 2123, "address": "test", "user_id": "da2fef24-f509-11eb-9a03-0242ac130003", "rectification": "24", "alarm_message_ids": "{4}", "is_training": true, "request_id": "da2fef24-f509-11eb-9a03-0242ac130002", "user_role": "d", "scenario_uuid": "00000000-0000-0000-0000-0f93131b988f" }