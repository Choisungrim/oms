package com.humuson.oms.service;

import com.humuson.oms.cache.SystemOrderCache;
import com.humuson.oms.entity.OrderVO;
import com.humuson.oms.exception.CustomException;
import com.humuson.oms.util.constants.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderIntegration orderIntegration;

    @Override
    public void saveOrders(List<OrderVO> orders) {
        for (OrderVO order : orders) {
            if (isValidOrderStatus(order.getOrderStatus()))
                SystemOrderCache.saveOrder(order);
            else
                throw new CustomException(order);
        }
    }

    public void saveOrder(OrderVO order) {
        if (isValidOrderStatus(order.getOrderStatus()))
            SystemOrderCache.saveOrder(order);
        else
            throw new CustomException(order);
    }

    @Override
    public OrderVO getOrderById(String orderId) {
        return SystemOrderCache.getOrder(orderId);
    }

    @Override
    public List<OrderVO> getAllOrderAsList() {
        return SystemOrderCache.getAllOrdersAsList();
    }

    @Override
    public Map<String, OrderVO> getAllOrdersAsMap() {
        return SystemOrderCache.getAllOrdersAsMap();
    }

    public List<OrderVO> fetchOrdersFromExternal(String url) {
        return orderIntegration.fetchOrdersFromExternalSystem(url);
    }

    public Map<String,OrderVO> fetchOrderMapsFromExternalSystem(String url) {
        return orderIntegration.fetchOrderMapsFromExternalSystem(url);
    }

    public void sendOrdersToExternalSystem(String url) {
        orderIntegration.sendOrdersToExternalSystem(url, getAllOrderAsList());
    }

    private boolean isValidOrderStatus(String status) {
        return status.equalsIgnoreCase(SystemConstants.OrderState.CREATED) ||
                status.equalsIgnoreCase(SystemConstants.OrderState.RUNNING) ||
                status.equalsIgnoreCase(SystemConstants.OrderState.PENDING) ||
                status.equalsIgnoreCase(SystemConstants.OrderState.SHIPPING) ||
                status.equalsIgnoreCase(SystemConstants.OrderState.COMPLETED);
    }
}
