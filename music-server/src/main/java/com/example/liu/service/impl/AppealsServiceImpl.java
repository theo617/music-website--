package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.AppealsMapper;
import com.example.liu.mapper.ComplaintsMapper;
import com.example.liu.model.domain.Appeals;
import com.example.liu.model.domain.Complaints;
import com.example.liu.model.request.AppealStatusUpdateRequest;
import com.example.liu.model.request.AppealsRequest;
import com.example.liu.service.AppealsService;
import org.ehcache.sizeof.SizeOf;
import org.hamcrest.core.AllOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 11:52
 */
@Service
public class AppealsServiceImpl extends ServiceImpl<AppealsMapper, Appeals> implements AppealsService {

    @Autowired
    private AppealsMapper appealsMapper;
    @Autowired
    private ComplaintsMapper complaintsMapper;

    @Override
    public R submitAppeals(AppealsRequest appealsRequest) {
        Appeals appeals = new Appeals();

        appeals.setId(appealsRequest.getId());

        int complaintId = appealsRequest.getComplaintId();
        QueryWrapper<Complaints> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", complaintId);
        List<Complaints> result = complaintsMapper.selectList(queryWrapper);
        if (result == null || result.isEmpty()) {
            return R.error("不存在该投诉信息");
        }

        appeals.setComplaintId(complaintId);
        appeals.setUserId(appealsRequest.getUserId());
        appeals.setReason(appealsRequest.getReason());
        appeals.setStatus(appealsRequest.getStatus());
        appeals.setCreateAt(appealsRequest.getCreateAt());
        appeals.setUpdateAt(appealsRequest.getUpdateAt());

        if (appealsMapper.insert(appeals) > 0) {
            return R.success("提交申诉成功");
        } else {
            return R.error("提交申诉失败");
        }
    }

    @Override
    public R viewAllAppeals() {
        List<Appeals> allAppeals = appealsMapper.selectList(null);
        if (allAppeals == null || allAppeals.size() == 0) {
            return R.error("当前没有申诉信息");
        } else {
            return R.success("查询成功", allAppeals);
        }
    }

    @Override
    public R updateAppealStatus(int id, AppealStatusUpdateRequest appealStatusUpdateRequest) {
        try {
            Appeals appeals = appealsMapper.selectById(id);
            if (appeals == null) {
                return R.error("申诉信息不存在");
            }
            appeals.setStatus(appealStatusUpdateRequest.getStatus());
            appeals.setUpdateAt(appealStatusUpdateRequest.getUpdateAt());
            if (appealsMapper.updateById(appeals) > 0) {
                return R.success("申诉状态更新成功");
            } else {
                return R.error("申诉状态更新失败");
            }
        } catch (Exception e) {
            return R.error("出现错误:" + e.getMessage());
        }
    }
}
