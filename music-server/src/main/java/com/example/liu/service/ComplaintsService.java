package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.Complaints;
import com.example.liu.model.request.ComplaintStatusUpdateRequest;
import com.example.liu.model.request.ComplaintsRequest;

import java.util.List;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 8:58
 */
public interface ComplaintsService extends IService<Complaints> {
    // 提交投诉
    R submitComplaints(ComplaintsRequest complaintsRequest);

    // 查看投诉
    R viewAllComplaints();

    // 更新投诉状态
    R updateComplaintStatus(int id, ComplaintStatusUpdateRequest complaintStatusUpdateRequest);
}
