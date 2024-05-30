package com.example.liu.controller;
import com.alibaba.excel.EasyExcel;
import com.example.liu.common.R;
import com.example.liu.model.domain.SongListConsumer;
import com.example.liu.model.request.ListSongConsumerRequest;
import com.example.liu.service.ListSongConsumerService;
import com.example.liu.service.SongListConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
@RestController
public class ListSongConsumerController {
    @Autowired
    private SongListConsumerService service;
    @Autowired
    private ListSongConsumerService listSongConsumerService;
    // 给歌单添加歌曲
    @PostMapping("/listSongConsumer/add")
    public R addListSongConsumer(@RequestBody ListSongConsumerRequest addListSongConsumerRequest) {
        return listSongConsumerService.addListSongConsumer(addListSongConsumerRequest);
    }

    // 删除歌单里的歌曲
    @GetMapping("/listSongConsumer/delete")
    public R deleteListSongConsumer(@RequestParam int songId, @RequestParam int songListConsumerId) {
        return listSongConsumerService.deleteListSongConsumer(songId, songListConsumerId);
    }

    // 返回歌单里指定歌单 ID 的歌曲
    @GetMapping("/listSongConsumer/detail")
    public R listSongConsumerOfSongId(@RequestParam int songListConsumerId) {
        return listSongConsumerService.listSongConsumerOfSongId(songListConsumerId);
    }

    // 更新歌单里面的歌曲信息
    @PostMapping("/listSongConsumer/update")
    public R updateListSongConsumerMsg(@RequestBody ListSongConsumerRequest updateListSongConsumerRequest) {
        return listSongConsumerService.updateListSongConsumerMsg(updateListSongConsumerRequest);
    }
    // 导出歌单
    @GetMapping("consumer/excle")
    public ResponseEntity<Resource> getConsumerExcle(HttpServletRequest request) throws IOException {
        String fileName = "SongListConsumer" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, SongListConsumer.class).sheet("模板").doWrite(data());
        File file = new File(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        byte[] content = Files.readAllBytes(file.toPath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(content.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    private List<SongListConsumer> data() {
        return service.findAllSong();
    }
}
