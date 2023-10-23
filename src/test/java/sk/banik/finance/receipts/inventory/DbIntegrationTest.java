package sk.banik.finance.receipts.inventory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import sk.banik.finance.receipts.inventory.model.Receipt;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DbIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @ServiceConnection
        PostgreSQLContainer<?> postgresContainer() {
            return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("test-db-name")
                    .withUsername("test-user")
                    .withPassword("test-password");
        }
    }

    @Autowired
    private JdbcTemplate template;

    @AfterEach
    void tearDown() {
        template.execute("delete from receipt");
    }

    @Test
    void contextLoads() {
        template
                .query("select * from receipt",
                        (rs, rowNum) -> {
                            Receipt receipt = new Receipt();
                            receipt.setCode(rs.getString("code"));
                            return receipt;
                        })
                .forEach(System.out::println);
    }

    @Test
    void canWriteReceiptsToDB() {
        Integer receiptsBeforeInsert = template.queryForObject(
                "select count(*) from receipt", Integer.class);
        assert receiptsBeforeInsert != null;

        template.execute("insert into receipt (code) values ('001')");

        Integer receiptsCountAfterInsert = template.queryForObject(
                "select count(*) from receipt", Integer.class);

        assertEquals(receiptsBeforeInsert + 1, receiptsCountAfterInsert,
                "Receipts count should increase after inserting to DB");
    }

    @Test
    void canReadReceiptsFromDB() {
        template.batchUpdate(
                "insert into receipt (code) values ('001')",
                "insert into receipt (code) values ('002')"
        );

        List<Receipt> receipts = template.queryForStream("select * from receipt",
                (rs, rowNum) -> {
                    Receipt receipt = new Receipt();
                    receipt.setCode(rs.getString("code"));
                    return receipt;
                }).toList();

        Receipt expectedReceiptNumber1 = new Receipt();
        expectedReceiptNumber1.setCode("001");
        Receipt expectedReceiptNumber2 = new Receipt();
        expectedReceiptNumber2.setCode("002");
        assertThat(receipts).containsOnly(expectedReceiptNumber1, expectedReceiptNumber2);
    }
}
