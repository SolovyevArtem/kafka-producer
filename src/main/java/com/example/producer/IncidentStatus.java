package com.example.producer;


public enum IncidentStatus {
    Pending,  // need to be created in DB first because of task-incident foreign key
    Created,
    InProgress,
    Complete,
    Rejected
}
