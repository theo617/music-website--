package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.Song;
import com.example.liu.model.request.SongRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface SongService extends IService<Song> {

    R addSong (SongRequest addSongRequest,MultipartFile lrcfile,  MultipartFile mpfile);

    R updateSongMsg(SongRequest updateSongRequest);

    R updateSongUrl(MultipartFile urlFile, int id);

    R updateSongPic(MultipartFile urlFile, int id);

    R deleteSong(Integer id);

    R deleteSongByManager(Integer id, Integer complainterId);

    R unDeleteSongByManager(Integer id, Integer complainterId, Integer applealerId);

    R allSong();

    R songOfSingerId(Integer singerId);

    R songOfId(Integer id);

    R songOfSingerName(String name);

    R updateSongLrc(MultipartFile lrcFile, int id);

    void exportSongLrc(int id, HttpServletResponse response);
}
