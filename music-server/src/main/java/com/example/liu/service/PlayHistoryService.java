package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.PlayHistory;
import com.example.liu.model.request.PlayHistoryRequest;

/**
 * @author 544
 * @Description:
 * @date 2024/5/29 9:21
 */
public interface PlayHistoryService extends IService<PlayHistory> {
    R recodePlayHistory(PlayHistoryRequest playHistoryRequest);

    R getPlayHistory(Integer userId);
}
