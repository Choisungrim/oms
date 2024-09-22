package com.humuson.oms.service;

import com.humuson.oms.entity.OrderVO;
import com.humuson.oms.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderIntegrationImpl implements OrderIntegration {

    private RestTemplate restTemplate;

    @Autowired
    public void DataConvert(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<OrderVO> fetchOrdersFromExternalSystem(String url) {
        ResponseEntity<List<OrderVO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderVO>>() {}
        );

        // 응답 상태 코드 확인
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException("외부 시스템에서 주문 데이터를 가져오는 데 실패했습니다. 상태 코드: " + response.getStatusCode());
        }

        if (response.getBody() == null) {
            throw new CustomException("응답 본문이 null입니다.");
        }
        return response.getBody();
    }

    @Override
    public Map<String,OrderVO> fetchOrderMapsFromExternalSystem(String url) {
        ResponseEntity<List<OrderVO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderVO>>() {}
        );

        // 응답 상태 코드 확인
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException("외부 시스템에서 주문 데이터를 가져오는 데 실패했습니다. 상태 코드: " + response.getStatusCode());
        }

        if (response.getBody() == null) {
            throw new CustomException("응답 본문이 null입니다.");
        }

        return response.getBody().stream()
                .collect(Collectors.toMap(OrderVO::getOrderId, order -> order));
    }

    @Override
    public void sendOrdersToExternalSystem(String url, List<OrderVO> orders) {
        ResponseEntity<?>response = restTemplate.postForEntity(url, orders, Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException("외부 시스템에서 주문 데이터를 가져오는 데 실패했습니다. 상태 코드: " + response.getStatusCode());
        }

    }
}
