package com.humuson.oms.exception;

import com.humuson.oms.entity.OrderVO;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    public CustomException(OrderVO vo) {
        super("주문의 상태정보가 정확하지 않습니다 " + vo.getOrderId() + " 번 주문 " + vo.getOrderStatus() + "상태정보");
    }
    public CustomException(OrderVO vo, String orderId) {
        super("주문정보가 정확하지 않습니다 " + orderId + " 번 주문 ");
    }

    public CustomException(OrderVO vo, String orderId, String message) {
        super(message + orderId + " 번 주문 ");
    }

    public CustomException(String date, String methodName) {
        super("날짜의 형식이 올바르지 않습니다: " + date + " (메소드: " + methodName + ")");
    }
}