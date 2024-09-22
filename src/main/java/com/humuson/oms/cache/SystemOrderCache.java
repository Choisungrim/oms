package com.humuson.oms.cache;

import com.humuson.oms.entity.OrderVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemOrderCache {
    private static final Map<String, OrderVO> orderRepository = new HashMap<>();

    // 주문 저장
    public static void saveOrder(OrderVO order) {
        orderRepository.put(order.getOrderId(), order);
    }

    // 주문 조회
    public static OrderVO getOrder(String orderId) {
        if (orderRepository.containsKey(orderId))
            return orderRepository.get(orderId);
        else
            return null;
    }

    // 모든 주문 조회 (맵 형태)
    public static Map<String, OrderVO> getAllOrdersAsMap() {
        return new HashMap<>(orderRepository);
    }

    // 모든 주문 조회 (리스트 형태)
    public static List<OrderVO> getAllOrdersAsList() {
        return new ArrayList<>(orderRepository.values());
    }

    // 주문 제거
    public static void removeOrder(String orderId) {
        orderRepository.remove(orderId);
    }

}
