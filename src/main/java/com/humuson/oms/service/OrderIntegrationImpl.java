package com.humuson.oms.service;

import com.humuson.oms.entity.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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
        return response.getBody();
    }

    @Override
    public Map<String,OrderVO> fetchOrderMapsFromExternalSystem(String url) {
        ResponseEntity<Map<String,OrderVO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String,OrderVO>>() {}
        );
        return response.getBody();
    }

    @Override
    public void sendOrdersToExternalSystem(String url, List<OrderVO> orders) {
        restTemplate.postForEntity(url, orders, Void.class);
    }
}
