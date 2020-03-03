package com.example.apps.mega.model;

import java.io.Serializable;
import java.util.List;

public class ApiResponse implements Serializable {

    private Boolean success;

    private ApiError error;

    private String terms;

    private String privacy;

    private Long timestamp;

    private String source;

    private List<Quote> quotes;
}
