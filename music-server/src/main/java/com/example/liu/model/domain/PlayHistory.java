package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 544
 * @Description:
 * @date 2024/5/29 9:08
 */

@TableName(value = "play_history")
@Data
public class PlayHistory implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private int userId;

    private int songId;

    private int duration;

    private int playCount;

    private Timestamp playTimeStamp;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
