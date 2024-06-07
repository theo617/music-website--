package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.Follow;
import com.example.liu.model.request.FollowRequest;
import org.springframework.web.bind.annotation.RequestParam;

public interface FollowService extends IService<Follow> {
    R addFollow(FollowRequest followRequest);
    R deleteFollow(@RequestParam Integer followerId, @RequestParam Integer followedId);

    R listFollowByFollowedId(Integer followedId);

    R listFollowByFollowerId(Integer followedId);

}
