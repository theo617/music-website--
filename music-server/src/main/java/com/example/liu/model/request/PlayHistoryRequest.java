package com.example.liu.model.request;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 544
 * @Description:
 * @date 2024/5/29 9:16
 */
@Data
public class PlayHistoryRequest {
    private int id;

    private int userId;

    private int songId;

    private int singerId;

    private int duration;

    private int playCount;

    private Timestamp playTimeStamp;
}
