package com.example.liu.controller;

import com.example.liu.common.R;
import com.example.liu.model.request.SongRequest;
import com.example.liu.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SongController {

    @Autowired
    private SongService songService;


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大10M,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize(DataSize.of(20, DataUnit.MEGABYTES));
        // 设置总上传数据总大小10M
        factory.setMaxRequestSize(DataSize.of(20, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }



    // 添加歌曲
    @PostMapping("/song/add")
    public R addSong(SongRequest addSongRequest,@RequestParam("lrcfile") MultipartFile lrcfile, @RequestParam("file") MultipartFile mpfile) {
        return songService.addSong(addSongRequest,lrcfile,mpfile);
    }

    // 删除歌曲
    @PostMapping("/song/delete")
    public R deleteSong(@RequestParam int id) {
        return songService.deleteSong(id);
    }

    @PostMapping("/song/deleteByManager")
    public R deleteSongByManager(@RequestParam int id, @RequestParam int complainterId) {
        return songService.deleteSongByManager(id, complainterId);
    }

    @GetMapping("/song/undeleteByManager")
    public R undeleteSongByManager(@RequestParam int id, @RequestParam int complainterId, @RequestParam int applealerId) {
        return songService.unDeleteSongByManager(id, complainterId, applealerId);
    }

    // 返回所有歌曲
    @GetMapping("/song")
    public R allSong() {
        return songService.allSong();
    }

    //TODO ok
    // 返回指定歌曲ID的歌曲
    @GetMapping("/song/detail")
    public R songOfId(@RequestParam int id) {
        return songService.songOfId(id);
    }

    // 返回指定歌手ID的歌曲
    @GetMapping("/song/singer/detail")
    public R songOfSingerId(@RequestParam int singerId) {
        return songService.songOfSingerId(singerId);
    }


    // 返回指定歌手名的歌曲
    @GetMapping("/song/singerName/detail")
    public R songOfSingerName(@RequestParam String name) {
        return songService.songOfSingerName('%' + name + '%');
    }

    // 更新歌曲信息
    @PostMapping("/song/update")
    public R updateSongMsg(@RequestBody SongRequest updateSongRequest) {
        return songService.updateSongMsg(updateSongRequest);
    }

    // 更新歌曲图片
    @PostMapping("/song/img/update")
    public R updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        return songService.updateSongPic(urlFile, id);
    }

    // 更新歌曲
    @PostMapping("/song/url/update")
    public R updateSongUrl(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        return songService.updateSongUrl(urlFile, id);
    }
    ///song/lrc/update
    //更新歌词
    @PostMapping("/song/lrc/update")
    public R updateSongLrc(@RequestParam("file") MultipartFile lrcFile, @RequestParam("id") int id) {
        return songService.updateSongLrc(lrcFile, id);
    }

    @GetMapping("/exportLrc")
    public void exportSongLrc(@RequestParam("id") int id, HttpServletResponse response) {
        songService.exportSongLrc(id, response);
    }

}
