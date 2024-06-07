package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.*;
import com.example.liu.model.domain.*;
import com.example.liu.model.request.ComplaintStatusUpdateRequest;
import com.example.liu.model.request.ComplaintsRequest;
import com.example.liu.model.request.NotificationRequest;
import com.example.liu.service.ComplaintsService;
import com.example.liu.service.NotificationService;
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
    private SongListConsumerMapper songListConsumerMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SingerMapper singerMapper;

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

        int targetId = complaintsRequest.getTargetId();
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
            //获取用户名投诉对象等等
            String userName = "";
            String complaintedObject = "";
            Integer complaintedUserId = 0;
            String complaintedUserName = "";
            Boolean creatorExist = false;
            Boolean isSong = true;
            userName = consumer.getUsername();
            if (targetType.toString().equals("SONG")) {
                Song song = songMapper.selectById(targetId);
                System.out.println(targetId);
                if (song == null) {
                    return R.error("不存在该歌曲");
                }
                complaintedObject = song.getName();
                Integer singerId = 0;
                singerId = song.getSingerId();
                String singerName = "";
                Singer singer = singerMapper.selectById(singerId);
                singerName = singer.getName();
                QueryWrapper<Consumer> consumerQueryWrapper = new QueryWrapper<>();
                consumerQueryWrapper.eq("username", singerName);
                consumer = consumerMapper.selectOne(consumerQueryWrapper);
                if (consumer != null) {
                    creatorExist = true;
                    complaintedUserId = consumer.getId();
                    complaintedUserName = consumer.getUsername();
                }
            } else {
                isSong = false;
             SongListConsumer songListConsumer = songListConsumerMapper.selectById(targetId);
                if (songListConsumer == null) {
                    return R.error("不存在该歌单");
                }
                creatorExist = true;
                complaintedObject = songListConsumer.getTitle();
                complaintedUserId = songListConsumer.getUserId();
            }
            //投诉者
            NotificationRequest notificationRequest1 = new NotificationRequest();
            notificationRequest1.setUserId(userId);
            notificationRequest1.setUserType("consumer");
            if(isSong){
                notificationRequest1.setMessage("您对歌曲 \"" + complaintedObject + "\"的投诉已提交");
            } else {
                notificationRequest1.setMessage("您对歌单 \"" + complaintedObject + "\"的投诉已提交");
            }

            notificationRequest1.setType(2);
            notificationService.addNotification(notificationRequest1);

            //管理端
            NotificationRequest notificationRequest2 = new NotificationRequest();
            notificationRequest2.setUserId(null);
            notificationRequest2.setUserType("manager");
            if(isSong){
                notificationRequest2.setMessage("用户 \"" + userId + "-" + userName + "\" 投诉了歌曲 \"" + complaintedObject + "\"。");
            } else {
                notificationRequest2.setMessage("用户 \"" + userId + "-" + userName + "\" 投诉了歌单 \"" + complaintedObject + "\"。");
            }

            notificationRequest2.setType(2);
            notificationService.addNotification(notificationRequest2);

            //被投诉者（若有）
            if(creatorExist){
                NotificationRequest notificationRequest3 = new NotificationRequest();
                notificationRequest3.setUserId(complaintedUserId);
                notificationRequest3.setUserType("consumer");
                if(isSong){
                    notificationRequest3.setMessage("您的歌曲 \"" + complaintedObject + "\" 被投诉！");
                } else {
                    notificationRequest3.setMessage("您的歌单 \"" + complaintedObject + "\" 被投诉！");
                }

                notificationRequest3.setType(2);
                notificationService.addNotification(notificationRequest3);
            }
            return R.success("提交投诉成功");
        } else {
            return R.error("提交投诉失败");
        }
    }

    @Override
    public R viewAllComplaints() {
        List<Complaints> allComplaints = complaintsMapper.selectList(null);
        if (allComplaints == null || allComplaints.isEmpty()) {
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
