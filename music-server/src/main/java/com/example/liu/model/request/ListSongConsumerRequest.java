package com.example.liu.model.request;

import lombok.Data;

/**
 * @Author 21241013
 * @Time : 2024/5/21 11:35
 **/
@Data
public class ListSongConsumerRequest {
    private Integer id;

    private Integer songId;

    private  Integer songListConsumerId;
}
