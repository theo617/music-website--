package com.example.liu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.ComplaintsMapper;
import com.example.liu.mapper.ConsumerMapper;
import com.example.liu.mapper.SongListMapper;
import com.example.liu.mapper.SongMapper;
import com.example.liu.model.domain.Complaints;
import com.example.liu.model.domain.Consumer;
import com.example.liu.model.domain.Song;
import com.example.liu.model.domain.SongList;
import com.example.liu.model.request.ComplaintStatusUpdateRequest;
import com.example.liu.model.request.ComplaintsRequest;
import com.example.liu.service.ComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 8:57
 */
@Service
public class ComplaintsServiceImpl extends ServiceImpl<ComplaintsMapper, Complaints> implements ComplaintsService {

    @Autowired
    private ComplaintsMapper complaintsMapper;
    @Autowired
    private ConsumerMapper consumerMapper;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private SongListMapper songListMapper;

    @Override
    public R submitComplaints(ComplaintsRequest complaintsRequest) {
        Complaints complaints = new Complaints();

        int userId = complaintsRequest.getUserId();
        Consumer consumer = consumerMapper.selectById(userId);
        if (consumer == null) {
            return R.error("不存在该用户");
        }
        complaints.setUserId(userId);

        Complaints.TargetType targetType = complaintsRequest.getTargetType();
        if (!(targetType.toString().equals("SONG") || targetType.toString().equals("PLAYLIST"))) {
            return R.error("收藏目标对象的类型有误");
        }
        complaints.setTargetType(targetType);

        int targetId = complaints.getTargetId();
//        if (targetType.toString().equals("SONG")) {
//            Song song = songMapper.selectById(targetId);
//            if (song == null) {
//                return R.error("不存在该歌曲");
//            }
//        } else {
//            SongList songList = songListMapper.selectById(targetId);
//            if (songList == null) {
//                return R.error("不存在该歌单");
//            }
//        }
        complaints.setTargetId(targetId);

        complaints.setReason(complaintsRequest.getReason());

        Complaints.Status status = complaintsRequest.getStatus();
        if (!(status.toString().equals("PENDING") || status.toString().equals("DISMISSED") || status.toString().equals("VIEWED"))) {
            return R.error("不存在该状态");
        }
        complaints.setStatus(status);

        complaints.setCreateAt(complaintsRequest.getCreateAt());
        complaints.setUpdateAt(complaintsRequest.getUpdateAt());

        if (complaintsMapper.insert(complaints) > 0) {
            return R.success("提交投诉成功");
        } else {
            return R.error("提交投诉失败");
        }
    }

    @Override
    public R viewAllComplaints() {
        List<Complaints> allComplaints = complaintsMapper.selectList(null);
        if (allComplaints == null || allComplaints.size() == 0) {
            return R.error("当前没有投诉信息");
        } else {
            return R.success("查询成功", allComplaints);
        }
    }

    @Override
    public R updateComplaintStatus(int id, ComplaintStatusUpdateRequest complaintStatusUpdateRequest) {
        Complaints complaints = complaintsMapper.selectById(id);
        if (complaints == null) {
            return R.error("投诉信息不存在");
        }
        complaints.setStatus(complaintStatusUpdateRequest.getStatus());
        complaints.setUpdateAt(complaintStatusUpdateRequest.getUpdateAt());
        if (complaintsMapper.updateById(complaints) > 0) {
            return R.success("投诉状态更新成功");
        } else {
            return R.error("投诉状态更新失败");
        }
    }
}
