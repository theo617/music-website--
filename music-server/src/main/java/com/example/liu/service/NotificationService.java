package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.Notification;

import java.util.List;

public interface NotificationService extends IService<Notification> {
    List<Notification> getManagerNotifications();
    List<Notification> getUserNotifications(int userId);
    R markNotificationAsRead(int id);
}
