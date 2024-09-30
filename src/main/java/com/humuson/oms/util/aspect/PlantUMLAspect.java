package com.humuson.oms.util.aspect;

import com.humuson.oms.util.annotation.PlantUML;
import com.humuson.oms.util.generator.UMLGenerator;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@Component
@Aspect
public class PlantUMLAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UMLGenerator umlGenerator;

    private static final String BASE_PACKAGE = "com.humuson.oms"; // 사용자 정의 패키지
    public static final String BASE_SAVE_PACKAGE = "src/main/resources/static/uml_diagram.puml";

    @PostConstruct
    public void collectAndSavePlantUML() {
        String umlCode = umlGenerator.generateUML(applicationContext, BASE_PACKAGE);
        generatePlantUML(umlCode);
    }

    private void generatePlantUML(String umlCode) {
        try (FileOutputStream fos = new FileOutputStream(BASE_SAVE_PACKAGE)) {
            fos.write(umlCode.getBytes(StandardCharsets.UTF_8));
            System.out.println("UML 다이어그램이 uml_diagram.puml로 생성되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
