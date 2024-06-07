package com.example.liu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.liu.common.R;
import com.example.liu.mapper.NotificationMapper;
import com.example.liu.model.domain.Notification;
import com.example.liu.model.request.NotificationRequest;
import com.example.liu.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public List<Notification> getManagerNotifications() {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", "manager");
        return notificationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Notification> getConsumerNotifications(int userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("user_type", "consumer");
        return notificationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Notification> getNotifications() {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        return notificationMapper.selectList(queryWrapper);
    }

    @Override
    public R addNotification(NotificationRequest addNotificationRequest){
        Notification notification = new Notification();
        BeanUtils.copyProperties(addNotificationRequest, notification);
        if (notificationMapper.insert(notification) > 0) {
            return R.success("添加信息成功");
        } else {
            return R.error("添加信息失败");
        }

    }

    @Override
    public R markNotificationAsRead(int id) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null) {
            return R.error("通知不存在");
        }
        notification.setIsRead(true);
        if (notificationMapper.updateById(notification) > 0) {
            return R.success("标记成功");
        } else {
            return R.error("标记失败");
        }
    }
}
