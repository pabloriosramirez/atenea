package online.grisk.atenea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:integration.cfg.xml")
public class AteneaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AteneaApplication.class, args);
    }

}
