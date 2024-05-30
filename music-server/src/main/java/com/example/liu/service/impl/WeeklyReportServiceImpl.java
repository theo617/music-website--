package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.ConsumerMapper;
import com.example.liu.mapper.WeeklyReportMapper;
import com.example.liu.model.domain.Consumer;
import com.example.liu.model.domain.WeeklyReport;
import com.example.liu.model.request.WeeklyReportRequest;
import com.example.liu.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 17:12
 */
@Service
public class WeeklyReportServiceImpl extends ServiceImpl<WeeklyReportMapper, WeeklyReport> implements WeeklyReportService {

    @Autowired
    private WeeklyReportMapper weeklyReportMapper;
    @Autowired
    private ConsumerMapper consumerMapper;

    @Override
    public R generateWeeklyReport(WeeklyReportRequest weeklyReportRequest) {
        WeeklyReport weeklyReport = new WeeklyReport();

        int userId = weeklyReportRequest.getUserId();
        Consumer consumer = consumerMapper.selectById(userId);
        if (consumer == null) {
            return R.error("不存在该用户");
        }
        weeklyReport.setUserId(userId);

        weeklyReport.setWeekStartDate(weeklyReportRequest.getWeekStartDate());
        weeklyReport.setWeekEndDate(weeklyReportRequest.getWeekEndDate());
        weeklyReport.setPlayCount(weeklyReportRequest.getPlayCount());
        weeklyReport.setPlayTotalTime(weeklyReportRequest.getPlayTotalTime());

        if (weeklyReportMapper.insert(weeklyReport) > 0) {
            return R.success("生成周报成功");
        } else {
            return R.error("生成周报失败");
        }
    }

    @Override
    public R getWeeklyReport(int userId) {
        QueryWrapper<WeeklyReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<WeeklyReport> result = weeklyReportMapper.selectList(queryWrapper);
        if (result == null || result.isEmpty()) {
            return R.error("当前没有用户周报");
        }
        return R.success("查询成功", result);
    }
}
