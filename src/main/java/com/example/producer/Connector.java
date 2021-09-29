package com.example.producer;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
public class Connector {
    private  String name;
    private  String state;
    private  int countTask;
    private List<Task> tasks;
    private  String  url;
}
