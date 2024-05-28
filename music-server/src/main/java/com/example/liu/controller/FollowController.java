package com.example.liu.controller;

import com.example.liu.common.R;
import com.example.liu.model.request.FollowRequest;
import com.example.liu.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class FollowController {

    @Autowired
    private FollowService followService;

    // 关注用户
    @PostMapping("/{id}/follow")
    public R addFollow(@RequestBody FollowRequest followRequest) {
        return followService.addFollow(followRequest);
    }

    // 取消关注
    @DeleteMapping("/{id}/follow")
    public R deleteFollow(@RequestBody FollowRequest followRequest) {
        return followService.deleteFollow(followRequest);
    }
}
