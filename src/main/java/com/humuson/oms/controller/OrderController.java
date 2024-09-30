package com.humuson.oms.controller;

import com.humuson.oms.entity.OrderVO;
import com.humuson.oms.exception.CustomException;
import com.humuson.oms.service.OrderServiceImpl;
import com.humuson.oms.util.annotation.PlantUML;
import com.humuson.oms.util.aspect.PlantUMLAspect;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "주문 관리 API")
@PlantUML
public class OrderController {

    private final OrderServiceImpl orderService;

    @Autowired
    private PlantUMLAspect plantUMLAspect;

    @Autowired
    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderService = orderServiceImpl;
    }

    /**
     * 전체 주문 데이터를 저장합니다.
     *
     * @param orders 저장할 주문 리스트
     * @return 저장 결과 메시지
     */
    @PostMapping("/import")
    @Operation(summary = "전체 주문 저장", description = "주문 리스트를 저장합니다.")
    public ResponseEntity<String> setOrders(@Validated @RequestBody List<OrderVO> orders) {
        orderService.saveOrders(orders);
        return ResponseEntity.ok("주문 데이터가 저장되었습니다.");
    }
    /**
     * 단일 주문 데이터를 저장합니다.
     *
     * @param order 저장할 주문 정보
     * @return 저장 결과 메시지
     */
    @PostMapping("/importSingle")
    @Operation(summary = "단일 주문 저장", description = "주문 리스트를 저장합니다.")
    public ResponseEntity<String> setOrder(@Validated @RequestBody OrderVO order) {
        orderService.saveOrder(order);
        return ResponseEntity.ok("주문 데이터가 저장되었습니다.");
    }

    /**
     * 주문 ID를 통해 주문을 조회합니다.
     *
     * @param orderId 조회할 주문 ID
     * @return 주문 정보
     */
    @PostMapping("/{orderId}")
    @Operation(summary = "단일 주문 조회", description = "주문 리스트를 조회합니다.")
    public ResponseEntity<OrderVO> getOrder(@PathVariable String orderId) {
        OrderVO order = orderService.getOrderById(orderId);
        if (order == null)
            throw new CustomException(new OrderVO(),orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * 모든 주문 데이터를 조회합니다.
     *
     * @return 주문 리스트
     */
    @GetMapping("/export")
    @Operation(summary = "전체 주문 조회", description = "주문 리스트를 조회합니다.")
    public ResponseEntity<List<OrderVO>> getOrderAll() {
        return ResponseEntity.ok(orderService.getAllOrderAsList());
    }

    /**
     * 모든 주문 데이터를 맵 형태로 조회합니다.
     *
     * @return 주문 맵
     */
    @GetMapping("/exportMap")
    @Operation(summary = "전체 주문 조회", description = "주문 맵을 조회합니다.")
    public ResponseEntity<Map<String, OrderVO>> getOrderAllAsMap() {
        return ResponseEntity.ok(orderService.getAllOrdersAsMap());
    }

    /**
     * 외부 시스템에서 주문 데이터를 가져옵니다.
     *
     * @param url 외부 시스템의 URL
     * @return 주문 리스트
     */
    @PostMapping("/external/fetch")
    @Operation(summary = "외부 시스템에서 주문 데이터 저장", description = "외부 시스템에서 주문 데이터를 가져와 저장합니다")
    public ResponseEntity<List<OrderVO>> fetchOrdersFromExternal(@RequestParam String url) {
        List<OrderVO> orders = orderService.fetchOrdersFromExternal(url);
        setOrders(orders);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/external/fetchSingle")
    @Operation(summary = "외부 시스템에서 주문 데이터 저장", description = "외부 시스템에서 주문 데이터를 가져와 저장합니다")
    public ResponseEntity<OrderVO> fetchOrderFromExternal(@RequestParam String url, @RequestParam String orderId) {
        Map<String, OrderVO> orders = orderService.fetchOrderMapsFromExternalSystem(url);

        if(!orders.containsKey(orderId))
            throw new CustomException(new OrderVO(), "외부 시스템에 해당 주문번호가 없습니다. "+orderId);

        OrderVO order = orders.get(orderId);
        setOrder(order);

        return ResponseEntity.ok(order); // 주문 객체 반환
    }

    /**
     * 내부에 존재하는 주문 데이터를 외부 시스템으로 전송합니다.
     *
     * @param url 외부 시스템의 URL
     * @return 전송 결과 메시지
     */
    @PostMapping("/external/localDataSend")
    @Operation(summary = "내부에 존재하는 주문 데이터를 외부 시스템으로 전송", description = "내부에 존재하는 주문 데이터를 외부 시스템으로 전송")
    public ResponseEntity<String> sendHoldOrdersToExternalSystem(@RequestParam String url) {
        orderService.sendOrdersToExternalSystem(url);
        return ResponseEntity.ok("내부 주문 데이터가 외부 시스템으로 전송되었습니다.");
    }
}
