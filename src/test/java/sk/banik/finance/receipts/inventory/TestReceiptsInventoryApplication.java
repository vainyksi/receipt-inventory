package sk.banik.finance.receipts.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Collectors;

@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest
public class TestReceiptsInventoryApplication {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @ServiceConnection
        PostgreSQLContainer<?> postgresContainer() {
            return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        }

    }

    @Test
    void contextLoads(@Autowired JdbcTemplate template) {
        template
                .query("select * from receipts",
                        (rs, rowNum) -> new Receipt(rs.getInt("id"), rs.getString("code")))
                .forEach(System.out::println);
    }

}