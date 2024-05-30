package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 17:06
 */
@TableName(value = "weekly_report")
@Data
public class WeeklyReport implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private int userId;

    private int playCount;

    private int playTotalTime;

    private Date weekStartDate;

    private Date weekEndDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
