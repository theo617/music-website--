package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.PlayHistoryMapper;
import com.example.liu.model.domain.PlayHistory;
import com.example.liu.model.request.PlayHistoryRequest;
import com.example.liu.service.PlayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 544
 * @Description:记录播放历史
 * @date 2024/5/29 9:26
 */
@Service
public class PlayHistoryServiceImpl extends ServiceImpl<PlayHistoryMapper, PlayHistory> implements PlayHistoryService{

    @Autowired
    private PlayHistoryMapper playHistoryMapper;

    @Override
    public R recodePlayHistory(PlayHistoryRequest playHistoryRequest) {
        PlayHistory playHistory = new PlayHistory();
        playHistory.setId(playHistoryRequest.getId());
        playHistory.setUserId(playHistoryRequest.getUserId());
        playHistory.setSongId(playHistoryRequest.getSongId());
        playHistory.setDuration(playHistoryRequest.getDuration());
        playHistory.setPlayCount(playHistoryRequest.getPlayCount());
        playHistory.setPlayTimeStamp(playHistoryRequest.getPlayTimeStamp());
        if (playHistoryMapper.insert(playHistory) > 0) {
            return R.success("添加播放历史成功");
        } else {
            return R.error("添加播放历史失败");
        }
    }

    @Override
    public R getPlayHistory(int userId) {
        QueryWrapper<PlayHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return R.success("查询成功", playHistoryMapper.selectList(queryWrapper));
    }
}
