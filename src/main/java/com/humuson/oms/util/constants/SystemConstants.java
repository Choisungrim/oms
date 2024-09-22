package com.humuson.oms.util.constants;

public class SystemConstants {

    public static class OrderState {
        public static String CREATE = "create"; // 생성
        public static String PENDING = "pending"; // 진행 전
        public static String RUNNING = "running"; // 진행 중
        public static String SHIPPING = "shipping"; // 배송 중
        public static String COMPLETED = "completed"; // 진행 후
    }
}
