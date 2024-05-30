package com.example.liu.service;

import com.example.liu.model.domain.Order;
public interface OrderManager {
    void sendPassword(Order order,String reciveAddress);
    void sendCode(String code,String reciveAddress);
}