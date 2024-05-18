package com.example.liu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.controller.MinioUploadController;
import com.example.liu.mapper.SongListConsumerMapper;
import com.example.liu.model.domain.SongListConsumer;
import com.example.liu.model.request.SongListConsumerRequest;
import com.example.liu.service.SongListConsumerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SongListConsumerImpl extends ServiceImpl<SongListConsumerMapper, SongListConsumer> implements SongListConsumerService {

    @Autowired
    private SongListConsumerMapper songListConsumerMapper;

    @Value("${minio.bucket-name}")
    String bucketName;

    @Override
    public R updateSongListConsumerMsg(SongListConsumerRequest updateSongListConsumerRequest) {
        SongListConsumer songListConsumer = new SongListConsumer();
        BeanUtils.copyProperties(updateSongListConsumerRequest, songListConsumer);
        if (songListConsumerMapper.updateById(songListConsumer) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R deleteSongListConsumer(Integer id) {
        if (songListConsumerMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R allSongListConsumer() {
        return R.success(null, songListConsumerMapper.selectList(null));
    }

    @Override
    public List<SongListConsumer> findAllSong() {
        return songListConsumerMapper.selectList(null);
    }


    @Override
    public R likeTitle(String title) {
        QueryWrapper<SongListConsumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title",title);
        return R.success(null, songListConsumerMapper.selectList(queryWrapper));
    }

    @Override
    public R likeStyle(String style) {
        QueryWrapper<SongListConsumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("style",style);
        return R.success(null, songListConsumerMapper.selectList(queryWrapper));
    }

    @Override
    public R likeUserId(Integer user_id) {
        QueryWrapper<SongListConsumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_id",user_id);
        return R.success(null, songListConsumerMapper.selectList(queryWrapper));
    }

    @Override
    public R addSongListConsumer(SongListConsumerRequest addSongListConsumerRequest) {
        SongListConsumer songListConsumer = new SongListConsumer();
        BeanUtils.copyProperties(addSongListConsumerRequest, songListConsumer);
        String pic = "/img/songListPic/123.jpg";
        songListConsumer.setPic(pic);
        if (songListConsumerMapper.insert(songListConsumer) > 0) {
            return R.success("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @Override
    public R updateSongListConsumerImg(MultipartFile avatorFile, @RequestParam("id") int id) {
        String fileName =avatorFile.getOriginalFilename();
        String path="/"+bucketName+"/"+"songlist/";
        String imgPath = path + fileName;
        MinioUploadController.uploadSonglistConsumerImgFile(avatorFile);
        SongListConsumer songListConsumer = new SongListConsumer();
        songListConsumer.setId(id);
        songListConsumer.setPic(imgPath);
        if (songListConsumerMapper.updateById(songListConsumer) > 0) {
            return R.success("上传成功", imgPath);
        } else {
            return R.error("上传失败");
        }
    }
}
