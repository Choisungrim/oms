package com.humuson.oms.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "spring oms API 명세서",
                description = "Order Management System API 명세서",
                version = "v1",
                contact = @Contact(
                        name = "ChoiSungrim",
                        email = "wowilos@naver.com"
                )
        )
)
public class swaggerConfig {
}
