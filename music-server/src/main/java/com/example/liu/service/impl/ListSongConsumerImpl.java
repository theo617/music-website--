package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.ListSongConsumerMapper;
import com.example.liu.model.domain.ListSongConsumer;
import com.example.liu.model.request.ListSongConsumerRequest;
import com.example.liu.service.ListSongConsumerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSongConsumerImpl extends ServiceImpl<ListSongConsumerMapper, ListSongConsumer> implements ListSongConsumerService {
    @Autowired
    private  ListSongConsumerMapper listSongConsumerMapper;

    @Override
    public List<ListSongConsumer> allListSongConsumer() {
        return listSongConsumerMapper.selectList(null);
    }

    @Override
    public R updateListSongConsumerMsg(ListSongConsumerRequest updateListSongConsumerRequest) {
        ListSongConsumer listSongConsumer = new ListSongConsumer();
        BeanUtils.copyProperties(updateListSongConsumerRequest, listSongConsumer);
        if (listSongConsumerMapper.updateById(listSongConsumer) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R deleteListSongConsumer(Integer songId, Integer songListConsumerId) {
        QueryWrapper<ListSongConsumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_id", songId).eq("song_list_consumer_id", songListConsumerId);
        if (listSongConsumerMapper.delete(queryWrapper) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R addListSongConsumer(ListSongConsumerRequest addListSongConsumerRequest) {
        ListSongConsumer listSong = new ListSongConsumer();
        BeanUtils.copyProperties(addListSongConsumerRequest, listSong);
        if (listSongConsumerMapper.insert(listSong) > 0) {
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
