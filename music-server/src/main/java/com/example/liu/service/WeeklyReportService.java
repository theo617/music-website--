package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.WeeklyReport;
import com.example.liu.model.request.WeeklyReportRequest;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 17:13
 */
public interface WeeklyReportService extends IService<WeeklyReport> {
    // 生成周报
    R generateWeeklyReport(WeeklyReportRequest weeklyReportRequest);

    // 获取周报
    R getWeeklyReport(int userId);
}
