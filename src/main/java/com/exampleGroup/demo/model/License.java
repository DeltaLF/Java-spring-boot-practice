package com.exampleGroup.demo.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class License {
    private String name;
    private Map<String, Object> info;
    private List<Map<String, Object>> lists;
    private String expired;
    private Signature signature;
}
