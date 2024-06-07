package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@TableName(value = "song_list_consumer")
@Data
public class SongListConsumer {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String title;

    private String pic;

    private String style;

    private String introduction;

    public SongListConsumer() {}

    public SongListConsumer(SongListConsumerDeleted songListConsumerDeleted){
        this.id = songListConsumerDeleted.getId();
        this.userId = songListConsumerDeleted.getUserId();
        this.title = songListConsumerDeleted.getTitle();
        this.pic = songListConsumerDeleted.getPic();
        this.style = songListConsumerDeleted.getStyle();
        this.introduction = songListConsumerDeleted.getIntroduction();
    }



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
