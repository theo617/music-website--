package com.example.liu.model.request;

import lombok.Data;

/**
 * @Author 21241013
 * @Time : 2024/5/20 15:27
 **/
@Data
public class SongListConsumerRequest {
    private Integer id;

    private String title;

    private String pic;

    private String style;

    private String introduction;

    private Integer user_id;

}
