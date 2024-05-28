package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("notifications")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer adminId;
    private String message;
    private Integer type;
    private Timestamp createdAt;
    private Boolean isRead;
}
