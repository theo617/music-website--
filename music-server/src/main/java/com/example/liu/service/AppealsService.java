package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.Appeals;
import com.example.liu.model.request.AppealStatusUpdateRequest;
import com.example.liu.model.request.AppealsRequest;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 11:48
 */
public interface AppealsService extends IService<Appeals> {
    // 提交申诉
    R submitAppeals(AppealsRequest appealsRequest);

    // 查看申诉
    R viewAllAppeals();

    // 更新申诉状态
    R updateAppealStatus(int id, AppealStatusUpdateRequest appealStatusUpdateRequest);
}
