package com.example.liu.controller;

import com.example.liu.common.R;
import com.example.liu.mapper.WeeklyReportMapper;
import com.example.liu.model.request.WeeklyReportRequest;
import com.example.liu.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author 544
 * @Description:
 * @date 2024/5/30 16:40
 */
@RestController
public class WeeklyReportController {
    @Autowired
    private WeeklyReportService weeklyReportService;

    // 生成周报
    @PostMapping("/weeklyReport")
    public R generateWeeklyReport(@RequestBody WeeklyReportRequest weeklyReportRequest) {
        return weeklyReportService.generateWeeklyReport(weeklyReportRequest);
    }

    // 获取用户周报
    @GetMapping("/weeklyReport/user/{userId}")
    public R getWeeklyReport(@PathVariable int userId) {
        return weeklyReportService.getWeeklyReport(userId);
    }
}
