package com.example.producer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCommand {
    final private String taskTypeUuid;
    final private String taskUuid;
    final private String incidentUuid;
    final private String serviceUuid;
    final private String command;
}
