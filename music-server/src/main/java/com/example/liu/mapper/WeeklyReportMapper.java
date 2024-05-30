package com.example.liu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.liu.model.domain.WeeklyReport;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 16:41
 */
@Repository
public interface WeeklyReportMapper extends BaseMapper<WeeklyReport> {

}
