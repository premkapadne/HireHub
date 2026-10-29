package com.hirehub;

import com.hirehub.security.utils.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableConfigurationProperties(value =  {CorsProperties.class})
public class HireHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(HireHubApplication.class, args);
    }

}
