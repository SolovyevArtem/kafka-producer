package com.example.producer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class IncidentNotificationRecord {
    @NonNull
    private String uuid;
  //  @NonNull
    @JsonProperty("incident_type_uuid")
    private String incidentTypeUuid;

    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="UTC")
    //private final ZonedDateTime time = ZonedDateTime.now();

    private final String time= ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    @NonNull
    private String status;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private float latitude;

    @NonNull
    private float longitude;

    @NonNull
    private String address;

   // @NonNull
    @JsonProperty("user_id")
    private String userId;

    @NonNull
    private String rectification;

    //@NonNull
    @JsonProperty("alarm_message_ids")
    private String alarmMessageIds;

   // @NonNull
    @JsonProperty("is_training")
    private boolean isTraining;

   // @NonNull
    @JsonProperty("request_id")
    private String requestId;

   // @NonNull
    @JsonProperty("user_role")
    private String userRole;

   // @NonNull
    @JsonProperty("scenario_uuid")
    private String scenarioUuid;


}
