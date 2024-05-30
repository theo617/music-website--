package com.example.liu.model.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@TableName(value = "list_song_consumer")
@Data
public class ListSongConsumer implements Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id;

    private  Integer songId;

    private  Integer songListConsumerId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
