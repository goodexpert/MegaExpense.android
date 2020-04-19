package com.example.apps.mega.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ApiResponse implements Serializable {

    public Boolean success;

    public ApiError error;

    public String terms;

    public String privacy;

    public Long timestamp;

    public String source;

    public Map<String, Double> quotes;
}
