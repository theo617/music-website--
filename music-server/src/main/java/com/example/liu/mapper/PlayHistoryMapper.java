package com.example.liu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.liu.model.domain.PlayHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 544
 * @Description:
 * @date 2024/5/29 9:01
 */
@Mapper
public interface PlayHistoryMapper extends BaseMapper<PlayHistory> {
//    @Insert("INSERT INTO play_history (user_id, song_id, play_time_stamp, duration, play_count) VALUES (#{userId}, #{songId}, #{playTimeStamp}, #{duration}, #{playCount})")
//    int insert(PlayHistory playHistory);
}
