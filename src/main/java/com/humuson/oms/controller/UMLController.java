package com.humuson.oms.controller;

import com.humuson.oms.util.annotation.PlantUML;
import com.humuson.oms.util.facter.WebPageFetcher;
import com.humuson.oms.util.generator.UMLGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/UML")
@Tag(name = "UML management", description = "UML 자동생성 API")
@PlantUML
public class UMLController {

    private final UMLGenerator umlGenerator;

    @Autowired
    private ApplicationContext applicationContext;

    private final String PLANT_UML_SERVER = "http://www.plantuml.com/plantuml/png/";

    public UMLController(ApplicationContext applicationContext, UMLGenerator umlGenerator) {
        this.umlGenerator = umlGenerator;
    }

    @GetMapping("/api/application-context")
    public Map<String, Object> getApplicationContextInfo() {
        Map<String, Object> contextInfo = new HashMap<>();

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> beanClass = applicationContext.getBean(beanName).getClass();

            // 필드 정보
            if (beanClass.isAnnotationPresent(PlantUML.class)) {
                Map<String, Object> beanInfo = new HashMap<>();
                beanInfo.put("className", beanClass.getName());

                // 필드 정보 수집
                Field[] fields = beanClass.getDeclaredFields();
                Map<String, String> fieldInfo = new HashMap<>();
                for (Field field : fields) {
                    fieldInfo.put(field.getName(), field.getType().getSimpleName());
                }
                beanInfo.put("fields", fieldInfo);

                // 메서드 정보 수집
                Method[] methods = beanClass.getDeclaredMethods();
                Map<String, String> methodInfo = new HashMap<>();
                for (Method method : methods) {
                    methodInfo.put(method.getName(), method.getReturnType().getSimpleName());
                }
                beanInfo.put("methods", methodInfo);

                // 인터페이스 정보 수집
                Class<?>[] interfaces = beanClass.getInterfaces();
                Map<String, String> interfaceInfo = new HashMap<>();
                for (Class<?> iface : interfaces) {
                    interfaceInfo.put(iface.getSimpleName(), iface.getName());
                }
                beanInfo.put("interfaces", interfaceInfo);

                contextInfo.put(beanName, beanInfo);
            }
        }

        return contextInfo; // 빈 정보를 JSON 형태로 반환
    }


    @GetMapping("/generate-uml")
    public String generateUML(@RequestParam String targetUrl) {
        try {
            WebPageFetcher fetcher = new WebPageFetcher();
            String htmlSource = fetcher.fetchWebPageSource(targetUrl); // HTML 소스 가져오기
            String umlCode = umlGenerator.generateUMLFromHtml(htmlSource); // UML 코드 생성

            return umlCode; // 생성된 UML 코드를 반환
        } catch (IOException e) {
            return "Error fetching the web page: " + e.getMessage(); // 에러 메시지 반환
        }
    }
    @GetMapping("/generate-uml/app")
    public ResponseEntity<byte[]> generateUMLAPP(@RequestParam String appUrl) {
        // 1. UML 코드 생성
        String umlCode = umlGenerator.generateRestTargetUML(appUrl);
        String encodedUml = encodeUml(umlCode); // UML 코드 인코딩


        // 2. PlantUML 서버에 요청
        String plantUmlServerUrl = "https://www.plantuml.com/plantuml/png/";

        // POST 요청 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "umlcode=" + umlCode;
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        // PlantUML 서버에 요청
        ResponseEntity<byte[]> response = restTemplate.exchange(plantUmlServerUrl, HttpMethod.POST, requestEntity, byte[].class);
        System.out.println(response);
        // 응답 처리
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(response.getBody());
        } else if (response.getStatusCode() == HttpStatus.FOUND) {
            // 리디렉션 URL을 확인
            String redirectUrl = response.getHeaders().getLocation().toString();
            // 리디렉션된 URL에 대한 요청을 재전송
            ResponseEntity<byte[]> redirectResponse = restTemplate.exchange(redirectUrl, HttpMethod.GET, null, byte[].class);
            System.out.println(redirectResponse);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(redirectResponse.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
    }
    private String encodeUml(String umlCode) {
        // UML 코드를 Base64로 인코딩
        return java.util.Base64.getEncoder().encodeToString(umlCode.getBytes());
    }


    private void generatePlantUML(String umlCode) {
        // UML 코드를 파일에 저장하거나 다른 처리를 할 수 있습니다.
        System.out.println(umlCode); // 예시로 콘솔에 출력합니다.
    }
}

