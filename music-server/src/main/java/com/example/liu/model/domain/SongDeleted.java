package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName(value = "song_deleted")
public class SongDeleted {
    @TableId
    private Integer id;
    private Integer singerId;
    private String name;
    private String introduction;
    private Date createTime;
    private Date updateTime;
    private String pic;
    private String lyric;
    private String url;

    public SongDeleted() {}
    public  SongDeleted(Song song){
        this.id = song.getId();
        this.singerId = song.getSingerId();
        this.name = song.getName();
        this.introduction = song.getIntroduction();;
        this.createTime = song.getCreateTime();
        this.updateTime = song.getUpdateTime();
        this.pic = song.getPic();
        this.lyric = song.getLyric();
        this.url = song.getUrl();
    }
}
