package online.grisk.atenea;

import online.grisk.atenea.domain.entity.Microservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;

@SpringBootApplication
public class AteneaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AteneaApplication.class, args);
    }

    @Bean
    Microservice microserviceHefesto() {
        return new Microservice("hefesto", HttpMethod.POST, "/v1/rest/api/bureau", "hefesto", "GRisk.2019", new HashMap<>());
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofHours(1))
                .setReadTimeout(Duration.ofHours(1))
                .build();
    }

}
