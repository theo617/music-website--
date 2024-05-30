package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.ConsumerMapper;
import com.example.liu.mapper.PlayHistoryMapper;
import com.example.liu.mapper.SingerMapper;
import com.example.liu.mapper.SongMapper;
import com.example.liu.model.domain.Consumer;
import com.example.liu.model.domain.PlayHistory;
import com.example.liu.model.domain.Singer;
import com.example.liu.model.domain.Song;
import com.example.liu.model.request.PlayHistoryRequest;
import com.example.liu.service.PlayHistoryService;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 544
 * @Description:记录播放历史
 * @date 2024/5/29 9:26
 */
@Service
public class PlayHistoryServiceImpl extends ServiceImpl<PlayHistoryMapper, PlayHistory> implements PlayHistoryService{

    @Autowired
    private PlayHistoryMapper playHistoryMapper;
    @Autowired
    private ConsumerMapper consumerMapper;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private SingerMapper singerMapper;

    @Override
    public R recodePlayHistory(PlayHistoryRequest playHistoryRequest) {
        PlayHistory playHistory = new PlayHistory();

        int userId = playHistoryRequest.getUserId();
        Consumer consumer = consumerMapper.selectById(userId);
        if (consumer == null) {
            return R.error("不存在该用户");
        }
        playHistory.setUserId(userId);

        int songId = playHistoryRequest.getSongId();
        Song song = songMapper.selectById(songId);
        if (song == null) {
            return R.error("不存在该歌曲");
        }
        playHistory.setSongId(songId);

        int singerId = playHistoryRequest.getSingerId();
        Singer singer = singerMapper.selectById(singerId);
        if (singer == null) {
            return R.error("不存在该歌手");
        }
        playHistory.setSingerId(singerId);

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
        List<PlayHistory> result = playHistoryMapper.selectList(queryWrapper);
        if (result == null || result.isEmpty()) {
            return R.error("当前没有播放历史记录");
        }
        return R.success("查询成功", result);
    }
}
