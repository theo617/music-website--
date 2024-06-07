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
import net.sf.jsqlparser.util.validation.metadata.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static java.lang.System.*;

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

    @Override
    public R recodePlayHistory(PlayHistoryRequest playHistoryRequest) {
        PlayHistory playHistory = new PlayHistory();

        int userId = playHistoryRequest.getUserId();
        Consumer consumer = consumerMapper.selectById(userId);
        if (consumer == null) {
            return R.error("不存在该用户");
        }

        int songId = playHistoryRequest.getSongId();
        Song song = songMapper.selectById(songId);
        if (song == null) {
            return R.error("不存在该歌曲");
        }

        QueryWrapper<PlayHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("song_id", songId);

        PlayHistory oldPlayHistory = playHistoryMapper.selectOne(queryWrapper);
        if (oldPlayHistory != null) {   // 以前听过这首歌
            oldPlayHistory.setPlayCount(oldPlayHistory.getPlayCount() + 1);

            Instant now = Instant.now();
            Timestamp timestamp = java.sql.Timestamp.from(now);
            oldPlayHistory.setPlayTimeStamp(timestamp);

            if (playHistoryMapper.updateById(oldPlayHistory) > 0) {
                return R.success("更新播放历史成功");
            } else {
                return R.error("更新播放历史失败");
            }

        } else {
            playHistory.setUserId(userId);
            playHistory.setSongId(songId);
            playHistory.setPlayCount(playHistoryRequest.getPlayCount());
            playHistory.setPlayTimeStamp(playHistoryRequest.getPlayTimeStamp());
            if (playHistoryMapper.insert(playHistory) > 0) {
                return R.success("添加播放历史成功");
            } else {
                return R.error("添加播放历史失败");
            }
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
