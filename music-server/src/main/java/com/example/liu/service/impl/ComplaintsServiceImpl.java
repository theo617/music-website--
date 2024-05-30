package com.example.liu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.ComplaintsMapper;
import com.example.liu.model.domain.Complaints;
import com.example.liu.model.request.ComplaintStatusUpdateRequest;
import com.example.liu.model.request.ComplaintsRequest;
import com.example.liu.service.ComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 8:57
 */
@Service
public class ComplaintsServiceImpl extends ServiceImpl<ComplaintsMapper, Complaints> implements ComplaintsService {

    @Autowired
    private ComplaintsMapper complaintsMapper;

    @Override
    public R submitComplaints(ComplaintsRequest complaintsRequest) {
        Complaints complaints = new Complaints();

        complaints.setId(complaintsRequest.getId());
        complaints.setUserId(complaintsRequest.getUserId());
        complaints.setTargetType(complaintsRequest.getTargetType());
        complaints.setTargetId(complaintsRequest.getTargetId());
        complaints.setReason(complaintsRequest.getReason());
        complaints.setStatus(complaintsRequest.getStatus());
        complaints.setCreateAt(complaintsRequest.getCreateAt());
        complaints.setUpdateAt(complaintsRequest.getUpdateAt());

        if (complaintsMapper.insert(complaints) > 0) {
            return R.success("提交投诉成功");
        } else {
            return R.error("提交投诉失败");
        }
    }

    @Override
    public R viewAllComplaints() {
        List<Complaints> allComplaints = complaintsMapper.selectList(null);
        if (allComplaints == null || allComplaints.size() == 0) {
            return R.error("当前没有投诉信息");
        } else {
            return R.success("查询成功", allComplaints);
        }
    }

    @Override
    public R updateComplaintStatus(int id, ComplaintStatusUpdateRequest complaintStatusUpdateRequest) {
        Complaints complaints = complaintsMapper.selectById(id);
        if (complaints == null) {
            return R.error("投诉信息不存在");
        }
        complaints.setStatus(complaintStatusUpdateRequest.getStatus());
        complaints.setUpdateAt(complaintStatusUpdateRequest.getUpdateAt());
        if (complaintsMapper.updateById(complaints) > 0) {
            return R.success("投诉状态更新成功");
        } else {
            return R.error("投诉状态更新失败");
        }
    }
}
