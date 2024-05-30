package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 8:49
 */
@TableName(value = "complaints")
@Data
public class Complaints implements Serializable {

    public enum TargetType {
        SONG, PLAYLIST
    }

    public enum Status {
        PENDING, REVIEWED, DISMISSED
    }

    @TableId(type = IdType.AUTO)
    private int id;

    private int userId;

    private TargetType targetType;

    private int targetId;

    private String reason;

    private Status status;

    private Timestamp createAt;

    private Timestamp updateAt;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
