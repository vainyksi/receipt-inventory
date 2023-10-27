package sk.banik.finance.receipts.inventory;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import sk.banik.finance.receipts.inventory.model.Receipt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReceiptDbIT {

    @LocalServerPort
    private int portNumber;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @ServiceConnection
        PostgreSQLContainer<?> postgresContainer() {
            return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private TestRestTemplate restTemplate;

    @PostConstruct
    void initialize() {
        String baseUrl = "http://localhost:" + portNumber + "/";
        restTemplate = new TestRestTemplate(restTemplateBuilder.rootUri(baseUrl));
    }

    @Test
    void shouldStoreReceiptInDB() {
        List<Receipt> receiptsStoredBeforeInsertion = jdbcTemplate.query("select * from receipt", (rs, rowNum) -> {
            Receipt receipt = new Receipt();
            receipt.setCode(rs.getString("code"));
            return receipt;
        });
        Assertions.assertTrue(receiptsStoredBeforeInsertion.isEmpty());

        restTemplate.postForObject("/receipt/" + "newReceiptId1", null, String.class);
        restTemplate.postForObject("/receipt/" + "newReceiptId2", null, String.class);

        List<Receipt> receiptsStored = jdbcTemplate.query("select * from receipt", (rs, rowNum) -> {
            Receipt receipt = new Receipt();
            receipt.setCode(rs.getString("code"));
            return receipt;
        });

        assertFalse(receiptsStored.isEmpty());
        assertEquals("newReceiptId1", receiptsStored.get(0).getCode());
    }
}