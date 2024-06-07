package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.ConsumerMapper;
import com.example.liu.mapper.FollowMapper;
import com.example.liu.mapper.ListSongConsumerMapper;
import com.example.liu.mapper.SongListConsumerMapper;
import com.example.liu.model.domain.*;
import com.example.liu.model.request.ListSongConsumerRequest;
import com.example.liu.model.request.NotificationRequest;
import com.example.liu.service.ListSongConsumerService;
import com.example.liu.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSongConsumerImpl extends ServiceImpl<ListSongConsumerMapper, ListSongConsumer> implements ListSongConsumerService {
    @Autowired
    private  ListSongConsumerMapper listSongConsumerMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private SongListConsumerMapper songListConsumerMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<ListSongConsumer> allListSongConsumer() {
        return listSongConsumerMapper.selectList(null);
    }

    @Override
    public R updateListSongConsumerMsg(ListSongConsumerRequest updateListSongConsumerRequest) {
        ListSongConsumer listSongConsumer = new ListSongConsumer();
        BeanUtils.copyProperties(updateListSongConsumerRequest, listSongConsumer);

        if (listSongConsumerMapper.updateById(listSongConsumer) > 0) {
            // 歌单创建者ID和title
            Integer songListConsumerId = listSongConsumer.getSongListConsumerId();
            QueryWrapper<SongListConsumer> songListConsumerQueryWrapper = new QueryWrapper<>();
            songListConsumerQueryWrapper.eq("id", songListConsumerId);
            SongListConsumer songListConsumer = songListConsumerMapper.selectOne(songListConsumerQueryWrapper);
            Integer creatorId = songListConsumer.getUserId();
            String songListTitle = songListConsumer.getTitle();

            // 获取歌单创建者的用户名
            QueryWrapper<Consumer> queryWrapperUser = new QueryWrapper<>();
            queryWrapperUser.eq("id", creatorId);
            String username = ""; // 初始化用户名
            Consumer consumer = consumerMapper.selectOne(queryWrapperUser);
            if (consumer != null) {
                username = consumer.getUsername();
            }

            // 查询所有关注该歌单创建者的关注者
            QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("followed_id", creatorId);
            List<Follow> followers = followMapper.selectList(queryWrapper);

            // 遍历所有关注者并发送通知
            for (Follow follower : followers) {
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setUserId(follower.getFollowerId());
                notificationRequest.setUserType("consumer");
                notificationRequest.setMessage("您关注的用户 \"" + username + "\" 更新了歌单 \"" + songListTitle + "\", 快来看看吧！");
                notificationRequest.setType(1);
                notificationService.addNotification(notificationRequest);
            }
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R deleteListSongConsumer(Integer songId, Integer songListConsumerId) {
        QueryWrapper<ListSongConsumer> listSongConsumerQueryWrapper = new QueryWrapper<>();
        listSongConsumerQueryWrapper.eq("song_id", songId).eq("song_list_consumer_id", songListConsumerId);
        // 歌单创建者ID和title
        QueryWrapper<SongListConsumer> songListConsumerQueryWrapper = new QueryWrapper<>();
        songListConsumerQueryWrapper.eq("id", songListConsumerId);
        SongListConsumer songListConsumer = songListConsumerMapper.selectOne(songListConsumerQueryWrapper);
        Integer creatorId = songListConsumer.getUserId();
        String songListTitle = songListConsumer.getTitle();

        // 获取歌单创建者的用户名
        QueryWrapper<Consumer> queryWrapperUser = new QueryWrapper<>();
        queryWrapperUser.eq("id", creatorId);
        String username = ""; // 初始化用户名
        Consumer consumer = consumerMapper.selectOne(queryWrapperUser);
        if (consumer != null) {
            username = consumer.getUsername();
        }

        // 查询所有关注该歌单创建者的关注者
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("followed_id", creatorId);
        List<Follow> followers = followMapper.selectList(queryWrapper);

        // 遍历所有关注者并发送通知
        for (Follow follower : followers) {
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setUserId(follower.getFollowerId());
            notificationRequest.setUserType("consumer");
            notificationRequest.setMessage("您关注的用户 \"" + username + "\" 在歌单 \"" + songListTitle + "\"中删除了歌曲。");
            notificationRequest.setType(1);

            notificationService.addNotification(notificationRequest);
        }
        if (listSongConsumerMapper.delete(listSongConsumerQueryWrapper) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R addListSongConsumer(ListSongConsumerRequest addListSongConsumerRequest) {
        ListSongConsumer listSongConsumer = new ListSongConsumer();
        BeanUtils.copyProperties(addListSongConsumerRequest, listSongConsumer);
        if (listSongConsumerMapper.insert(listSongConsumer) > 0) {
            // 歌单创建者ID和title
            Integer songListConsumerId = listSongConsumer.getSongListConsumerId();
            QueryWrapper<SongListConsumer> songListConsumerQueryWrapper = new QueryWrapper<>();
            songListConsumerQueryWrapper.eq("id", songListConsumerId);
            SongListConsumer songListConsumer = songListConsumerMapper.selectOne(songListConsumerQueryWrapper);
            Integer creatorId = songListConsumer.getUserId();
            String songListTitle = songListConsumer.getTitle();

            // 获取歌单创建者的用户名
            QueryWrapper<Consumer> queryWrapperUser = new QueryWrapper<>();
            queryWrapperUser.eq("id", creatorId);
            String username = ""; // 初始化用户名
            Consumer consumer = consumerMapper.selectOne(queryWrapperUser);
            if (consumer != null) {
                username = consumer.getUsername();
            }

            // 查询所有关注该歌单创建者的关注者
            QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("followed_id", creatorId);
            List<Follow> followers = followMapper.selectList(queryWrapper);

            // 遍历所有关注者并发送通知
            for (Follow follower : followers) {
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setUserId(follower.getFollowerId());
                notificationRequest.setUserType("consumer");
                notificationRequest.setMessage("您关注的用户 \"" + username + "\" 更新了歌单 \"" + songListTitle + "\"，快来看看吧！");
                notificationRequest.setType(1);

                notificationService.addNotification(notificationRequest);
            }
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public R listSongConsumerOfSongId(Integer songListConsumerId) {
        QueryWrapper<ListSongConsumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_list_consumer_id", songListConsumerId);
        return R.success("查询成功", listSongConsumerMapper.selectList(queryWrapper));
    }

}
