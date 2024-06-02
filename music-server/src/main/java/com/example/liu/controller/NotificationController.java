package com.example.liu.controller;

import com.example.liu.common.R;
import com.example.liu.model.domain.Notification;
import com.example.liu.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // 获取管理端消息提醒
    @GetMapping("/manager")
    public List<Notification> getManagerNotifications() {
        return notificationService.getManagerNotifications();
    }

    // 获取用户消息提醒
    @GetMapping("/consumer")
    public List<Notification> getConsumerNotifications(@RequestParam int userId) {
        return notificationService.getConsumerNotifications(userId);
    }

    // 获取消息提醒表
    @GetMapping("/detail")
    public List<Notification> getNotifications() {
        return notificationService.getNotifications();
    }

    // 标记消息为已读
    @PutMapping("/{id}/read")
    public R markNotificationAsRead(@PathVariable int id) {
        return notificationService.markNotificationAsRead(id);
    }
}
