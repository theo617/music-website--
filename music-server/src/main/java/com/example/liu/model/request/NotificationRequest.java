package com.example.liu.model.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NotificationRequest {
    private Integer userId;

    private String userType;

    private String message;

    private Integer type;

}
