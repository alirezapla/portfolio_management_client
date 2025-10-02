package co.pla.portfoliomanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PortfolioManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioManagementApplication.class, args);
    }

}
