package com.example.liu.controller;

import com.example.liu.common.R;
import com.example.liu.model.request.PlayHistoryRequest;
import com.example.liu.service.PlayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 544
 * @Description:
 * @date 2024/5/29 9:06
 */
@RestController
public class PlayHistoryController {
    @Autowired
    private PlayHistoryService playHistoryService;

    // 记录播放历史
    @PostMapping("/playHistory")
    public R recodePlayHistory(@RequestBody PlayHistoryRequest playHistoryRequest) {
        return playHistoryService.recodePlayHistory(playHistoryRequest);
    }

    // 获取用户播放历史
    @GetMapping("playHistory/user/{userId}")
    public R getPlayHistory(@PathVariable Integer userId) {
        return playHistoryService.getPlayHistory(userId);
    }

}
