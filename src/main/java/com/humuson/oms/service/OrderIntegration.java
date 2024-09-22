package com.humuson.oms.service;

import com.humuson.oms.entity.OrderVO;

import java.util.List;
import java.util.Map;

public interface OrderIntegration {
    List<OrderVO> fetchOrdersFromExternalSystem(String url);

    void sendOrdersToExternalSystem(String url, List<OrderVO> orders);

    void sendOrdersToExternalSingle(String url, OrderVO orders);

    void sendOrdersToExternalSystemAsMap(String url, Map<String, OrderVO> orders);
}
