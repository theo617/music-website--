package com.example.liu.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("follow")
public class Follow {
    @TableId(type = IdType.AUTO)
    private  Integer id;

    private Integer followerId;

    private Integer followedId;

}
