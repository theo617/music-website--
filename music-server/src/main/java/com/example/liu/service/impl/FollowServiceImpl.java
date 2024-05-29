package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.FollowMapper;
import com.example.liu.model.domain.Follow;
import com.example.liu.model.domain.ListSongConsumer;
import com.example.liu.model.request.FollowRequest;
import com.example.liu.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Autowired
    private FollowMapper followMapper;


    @Override
    public R addFollow(FollowRequest followRequest) {
        // 检查是否已经存在相同的关注关系
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followRequest.getFollowerId());
        queryWrapper.eq("followed_id", followRequest.getFollowedId());

        Follow existingFollow = followMapper.selectOne(queryWrapper);
        if (existingFollow != null) {
            return R.error("已关注，请勿重复关注");
        }
        Follow follow = new Follow();
        follow.setFollowerId(followRequest.getFollowerId());
        follow.setFollowedId(followRequest.getFollowedId());
        if (followMapper.insert(follow) > 0) {
            return R.success("关注成功");
        } else {
            return R.error("关注失败");
        }
    }

    @Override
    public R deleteFollow(FollowRequest followRequest) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followRequest.getFollowerId())
                .eq("followed_id", followRequest.getFollowedId());
        if (followMapper.delete(queryWrapper) > 0) {
            return R.success("取消关注成功");
        } else {
            return R.error("取消关注失败");
        }
    }
    @Override
    public R listFollowByFollowedId(Integer followedId){
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("followed_id", followedId);
        return R.success("查询成功", followMapper.selectList(queryWrapper));
    }

    public R listFollowByFollowerId(Integer followerId){
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followerId);
        return R.success("查询成功", followMapper.selectList(queryWrapper));
    }

}
