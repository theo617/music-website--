package com.example.liu.controller;

import com.example.liu.common.R;
import com.example.liu.model.request.ComplaintStatusUpdateRequest;
import com.example.liu.model.request.ComplaintsRequest;
import com.example.liu.service.ComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 8:48
 */
@RestController
@RequestMapping
public class ComplaintsController {
    @Autowired
    private ComplaintsService complaintsService;

    // 提交投诉
    @PostMapping("/complaints")
    public R submitComplaints(@RequestBody ComplaintsRequest complaintsRequest) {
        return complaintsService.submitComplaints(complaintsRequest);
    }

    // 查看投诉
    @GetMapping("/complaints")
    public R viewAllComplaints() {
        return complaintsService.viewAllComplaints();
    }

    // 更新投诉状态
    @PutMapping("/complaints/{id}")
    public R updateComplaintStatus(@PathVariable int id, @RequestBody ComplaintStatusUpdateRequest complaintStatusUpdateRequest) {
        return complaintsService.updateComplaintStatus(id, complaintStatusUpdateRequest);
    }

}
