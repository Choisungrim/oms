package com.humuson.oms.service;

import com.humuson.oms.entity.OrderVO;

import java.util.List;
import java.util.Map;

public interface OrderService {
    public void saveOrders(List<OrderVO> orders);
    public void saveOrder(OrderVO order);

    public OrderVO getOrderById(String orderId);

    public List<OrderVO> getAllOrderAsList();

    public Map<String, OrderVO> getAllOrdersAsMap();
}
