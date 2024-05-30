package com.example.liu.model.request;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 17:11
 */
@Data
public class WeeklyReportRequest {

    private int userId;

    private int playCount;

    private int playTotalTime;

    private Date weekStartDate;

    private Date weekEndDate;
}
