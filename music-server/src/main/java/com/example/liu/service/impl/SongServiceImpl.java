package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.controller.MinioUploadController;
import com.example.liu.mapper.ConsumerMapper;
import com.example.liu.mapper.FollowMapper;
import com.example.liu.mapper.SingerMapper;
import com.example.liu.mapper.SongMapper;
import com.example.liu.model.domain.Consumer;
import com.example.liu.model.domain.Follow;
import com.example.liu.model.domain.Singer;
import com.example.liu.model.domain.Song;
import com.example.liu.model.request.NotificationRequest;
import com.example.liu.model.request.SongRequest;
import com.example.liu.service.NotificationService;
import com.example.liu.service.SongService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private SingerMapper singerMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private ConsumerMapper consumerMapper;
    @Autowired
    private NotificationService notificationService;
    @Value("${minio.bucket-name}")
    private String bucketName;
    @Autowired
    MinioClient minioClient;
    @Override
    public R allSong() {
        return R.success(null, songMapper.selectList(null));
    }

    @Override
    public R addSong(SongRequest addSongRequest, MultipartFile lrcfile, MultipartFile mpfile){
        Song song = new Song();
        BeanUtils.copyProperties(addSongRequest, song);
        String pic = "/img/songPic/tubiao.jpg";
        String fileName = mpfile.getOriginalFilename();
        String s = MinioUploadController.uploadFile(mpfile);
        String storeUrlPath = "/"+bucketName+"/" + fileName;
        song.setCreateTime(new Date());
        song.setUpdateTime(new Date());
        song.setPic(pic);
        song.setUrl(storeUrlPath);

        // Ensure song.getLyric() is not null
        if (song.getLyric() == null) {
            song.setLyric("");
        }

        if (lrcfile!=null&&(song.getLyric().equals("[00:00:00]暂无歌词"))){
            byte[] fileContent = new byte[0];
            try {
                fileContent = lrcfile.getBytes();
                String content = new String(fileContent, StandardCharsets.UTF_8);
                song.setLyric(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (s.equals("File uploaded successfully!")&&songMapper.insert(song) > 0) {
            // 歌曲创建者name和歌曲name
            Integer singerId = 0;
            singerId = song.getSingerId();
            String singerName = "";
            //QueryWrapper<Singer> singerQueryWrapper = new QueryWrapper<>();
            //singerQueryWrapper.eq("id", singerId);
            Singer singer = singerMapper.selectById(singerId);//selectOne(singerQueryWrapper)
            singerName = singer.getName();
            String songName = song.getName();

            //歌曲创建者ID
            QueryWrapper<Consumer> consumerQueryWrapper = new QueryWrapper<>();
            consumerQueryWrapper.eq("username", singerName);
            Integer creatorId = consumerMapper.selectOne(consumerQueryWrapper).getId();

            // 查询所有关注该歌单创建者的关注者
            QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("followed_id", creatorId);
            List<Follow> followers = followMapper.selectList(queryWrapper);

            // 遍历所有关注者并发送通知
            for (Follow follower : followers) {
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setUserId(follower.getFollowerId());
                notificationRequest.setUserType("consumer");
                notificationRequest.setMessage("您关注的用户 \"" + singerName + "\" 添加了歌曲 \"" + songName + "\", 快来看看吧！");
                notificationRequest.setType(2);
                notificationService.addNotification(notificationRequest);
            }
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R updateSongMsg(SongRequest updateSongRequest) {
        Song song = new Song();
        BeanUtils.copyProperties(updateSongRequest, song);
        if (songMapper.updateById(song) > 0) {
            return R.success("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    @Override
    public R updateSongUrl(MultipartFile urlFile, int id) {
        Song song = songMapper.selectById(id);
        String path = song.getUrl();
        String[] parts = path.split("/");
        String fileName = parts[parts.length - 1];

        RemoveObjectArgs removeObjectArgs=RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        fileName = urlFile.getOriginalFilename();
        String s = MinioUploadController.uploadFile(urlFile);
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        }
        String storeUrlPath = "/"+bucketName+"/" + fileName;
        song.setId(id);
        song.setUrl(storeUrlPath);
        song.setName(fileName);
        if (s.equals("File uploaded successfully!")&&songMapper.updateById(song) > 0) {
            return R.success("更新成功", storeUrlPath);
        } else {
            return R.error("更新失败");
        }
    }

    @Override
    public R updateSongPic(MultipartFile urlFile, int id) {
        String fileName =  urlFile.getOriginalFilename();
        String storeUrlPath = "/user01/singer/song/" + fileName;
        MinioUploadController.uploadSongImgFile(urlFile);
        Song song = new Song();
        song.setId(id);
        song.setPic(storeUrlPath);
        if (songMapper.updateById(song) > 0) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

    @Override
    public R deleteSong(Integer id) {
        Song song = songMapper.selectById(id);
        String path = song.getUrl();
        String[] parts = path.split("/");
        String fileName = parts[parts.length - 1];
        System.out.println(fileName);
        RemoveObjectArgs removeObjectArgs=RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        if (songMapper.deleteById(id) > 0) {
            try {
                minioClient.removeObject(removeObjectArgs);
            } catch (ErrorResponseException e) {
                throw new RuntimeException(e);
            } catch (InsufficientDataException e) {
                throw new RuntimeException(e);
            } catch (InternalException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (InvalidResponseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (ServerException e) {
                throw new RuntimeException(e);
            } catch (XmlParserException e) {
                throw new RuntimeException(e);
            }

            // 歌曲创建者name和歌曲name
            Integer singerId = 0;
            singerId = song.getSingerId();
            String singerName = "";
            //QueryWrapper<Singer> singerQueryWrapper = new QueryWrapper<>();
            //singerQueryWrapper.eq("id", singerId);
            Singer singer = singerMapper.selectById(singerId);//selectOne(singerQueryWrapper)
            singerName = singer.getName();
            String songName = song.getName();

            //歌曲创建者ID
            QueryWrapper<Consumer> consumerQueryWrapper = new QueryWrapper<>();
            consumerQueryWrapper.eq("username", singerName);
            Integer creatorId = consumerMapper.selectOne(consumerQueryWrapper).getId();

            // 查询所有关注该歌单创建者的关注者
            QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("followed_id", creatorId);
            List<Follow> followers = followMapper.selectList(queryWrapper);

            // 遍历所有关注者并发送通知
            for (Follow follower : followers) {
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setUserId(follower.getFollowerId());
                notificationRequest.setUserType("consumer");
                notificationRequest.setMessage("您关注的用户 \"" + singerName + "\" 删除了歌曲 \"" + songName + "\"。");
                notificationRequest.setType(2);
                notificationService.addNotification(notificationRequest);
            }
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R songOfSingerId(Integer singerId) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("singer_id",singerId);
        return R.success(null, songMapper.selectList(queryWrapper));
    }

    @Override
    public R songOfId(Integer id) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return R.success(null, songMapper.selectList(queryWrapper));
    }

    @Override
    public R songOfSingerName(String name) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        List<Song> songs = songMapper.selectList(queryWrapper);
        if (songs.isEmpty()){
            return R.error("添加失败，没有找到该歌,无法加入该歌单");
        }

        return R.success(null, songMapper.selectList(queryWrapper));
    }

    @Override
    public R updateSongLrc(MultipartFile lrcFile, int id) {
        Song song = songMapper.selectById(id);
        if (lrcFile!=null){
            byte[] fileContent = new byte[0];
            try {
                fileContent = lrcFile.getBytes();
                String content = new String(fileContent, StandardCharsets.UTF_8);
                song.setLyric(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (songMapper.updateById(song) > 0) {
            return R.success("更新成功");
        } else {
            return R.error("更新失败");
        }
    }
}
