package com.hirehub;

import com.hirehub.security.utils.CorsProperties;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableConfigurationProperties(value =  {CorsProperties.class})
@OpenAPIDefinition(
        info = @Info(
                title = "HireHub REST API Documentation",
                description = "HireHub REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Prem Kapadne",
                        email = "prmkapadne@gmail.com",
                        url = "https://www.linkedin.com/in/prem-shimpi/"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://github.com/premkapadne"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "HireHub REST API Documentation",
                url = "https://github.com/premkapadne/HireHub"
        )
)
public class HireHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(HireHubApplication.class, args);
    }

}
