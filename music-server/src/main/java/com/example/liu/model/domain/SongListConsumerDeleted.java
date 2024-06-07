package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "song_list_consumer_deleted")
@Data
public class SongListConsumerDeleted {
    @TableId
    private Integer id;
    private Integer userId;
    private String title;
    private String pic;
    private String style;
    private String introduction;
    // Default constructor
    public SongListConsumerDeleted() {}

    // Constructor to convert from SongListConsumer
    public SongListConsumerDeleted(SongListConsumer songListConsumer) {
        this.id = songListConsumer.getId();
        this.userId = songListConsumer.getUserId();
        this.title = songListConsumer.getTitle();
        this.pic = songListConsumer.getPic();
        this.style = songListConsumer.getStyle();
        this.introduction = songListConsumer.getIntroduction();
    }
}
