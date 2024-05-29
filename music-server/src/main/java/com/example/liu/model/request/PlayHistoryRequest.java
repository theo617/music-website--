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
    private Integer id;

    private Integer userId;

    private Integer songId;

    private Timestamp playTimeStamp;
}
