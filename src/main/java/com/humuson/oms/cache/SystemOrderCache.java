package com.humuson.oms.cache;

import com.humuson.oms.entity.OrderVO;
import com.humuson.oms.exception.CustomException;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemOrderCache {
    private static final Map<String, OrderVO> orderRepository = new HashMap<>();

    // 주문 저장
    public static void saveOrder(OrderVO order) {
        if (!orderRepository.containsKey(order.getOrderId()))
            orderRepository.put(order.getOrderId(), order);
        else
            throw new CustomException(order, order.getOrderId(), "중복된 주문 ");
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
        if (!orderRepository.containsKey(orderId))
        {
            OrderVO order = getOrder(orderId);
            orderRepository.remove(orderId, order);
        }
        else
            throw new CustomException(new OrderVO(), orderId);
    }

}
