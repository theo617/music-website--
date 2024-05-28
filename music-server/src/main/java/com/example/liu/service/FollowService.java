package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.Follow;
import com.example.liu.model.request.FollowRequest;

public interface FollowService extends IService<Follow> {
    R addFollow(FollowRequest followRequest);
    R deleteFollow(FollowRequest followRequest);
}
