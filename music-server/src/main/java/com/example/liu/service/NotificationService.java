package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.Notification;
import com.example.liu.model.request.NotificationRequest;

import java.util.List;

public interface NotificationService extends IService<Notification> {
    List<Notification> getManagerNotifications();
    List<Notification> getConsumerNotifications(int userId);
    List<Notification> getNotifications();

    R addNotification(NotificationRequest addNotificationRequest);
    R markNotificationAsRead(int id);
}
