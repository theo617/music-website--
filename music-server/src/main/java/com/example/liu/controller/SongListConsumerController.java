package com.example.liu.controller;

import com.example.liu.common.R;
import com.example.liu.model.request.SongListConsumerRequest;
import com.example.liu.service.SongListConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SongListConsumerController {

    @Autowired
    private SongListConsumerService songListConsumerService;
    @PostMapping ("/songListConsumer/add")
    public R addSongListConsumer(@RequestBody SongListConsumerRequest addSongListConsumerRequest){
        return songListConsumerService.addSongListConsumer(addSongListConsumerRequest);
    }

    @GetMapping("/songListConsumer/delete")
    public R deleteSongListConsumer(@RequestParam int id) {
        return songListConsumerService.deleteSongListConsumer(id);
    }

    @GetMapping("/songListConsumer")
    public R allSongListConsumer() {
        return songListConsumerService.allSongListConsumer();
    }

    @GetMapping("/songListConsumer/likeTitle/detail")
    public R songListConsumerOfLikeTitle(@RequestParam String title) {
        return songListConsumerService.likeTitle('%' + title + '%');
    }

    @GetMapping("/songListConsumer/likeStyle/detail")
    public R songListConsumerOfStyle(@RequestParam String style) {
        return songListConsumerService.likeStyle('%' + style + '%');
    }

    @GetMapping("/songListConsumer/likeUserId/detail")
    public R songListConsumerOfUserId(@RequestParam Integer userId) {
        return songListConsumerService.eqUserId(userId);
    }

    @PostMapping("/songListConsumer/update")
    public R updateSongListConsumerMsg(@RequestBody SongListConsumerRequest updateSongListConsumerRequest) {
        return songListConsumerService.updateSongListConsumerMsg(updateSongListConsumerRequest);
    }

    @PostMapping("/songListConsumer/img/update")
    public R updateSongListConsumerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        return songListConsumerService.updateSongListConsumerImg(avatorFile, id);
    }
    @GetMapping("/songListConsumer/byId/detail")
    public R songListConsumerOfId(@RequestParam Integer id) {
        return songListConsumerService.byId(id);
    }


}
