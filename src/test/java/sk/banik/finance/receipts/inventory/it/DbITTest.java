package sk.banik.finance.receipts.inventory.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import sk.banik.finance.receipts.inventory.model.Receipt;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DbITTest {

    // TODO make tests in `it` package able to run with real external DB, instead of test-containers

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
    }

    @Test
    void canWriteDataToDB() {
        Integer receiptsBeforeInsert = getNumberOfRowsFromDB();

        putSomeDataToDB();

        Integer receiptsCountAfterInsert = getNumberOfRowsFromDB();

        assertEquals(receiptsBeforeInsert + 1, receiptsCountAfterInsert,
                "Receipts count should increase after inserting to DB");
    }

    private Integer getNumberOfRowsFromDB() {
        Integer receiptsBeforeInsert = template.queryForObject(
                "select count(*) from receipt", Integer.class);
        return Objects.requireNonNullElse(receiptsBeforeInsert, 0);
    }

    private void putSomeDataToDB() {
        template.execute("insert into receipt (code) values ('001')");
    }

    @Test
    void canReadDataFromDB() {
        List<Receipt> dataStored = putMultipleRowsToDB();

        List<Receipt> dataLoaded = loadStoredDataFromDB();

        assertThat(dataLoaded).containsOnly(dataStored.toArray(Receipt[]::new));
    }

    private List<Receipt> putMultipleRowsToDB() {
        Receipt receipt1 = new Receipt();
        receipt1.setCode("001");
        Receipt receipt2 = new Receipt();
        receipt2.setCode("002");

        template.batchUpdate(
                "insert into receipt (code) values ('" + receipt1.getCode() + "')",
                "insert into receipt (code) values ('" + receipt2.getCode() + "')"
        );

        return List.of(receipt1, receipt2);
    }

    private List<Receipt> loadStoredDataFromDB() {
        List<Receipt> receipts = template.queryForStream("select * from receipt",
                (rs, rowNum) -> {
                    Receipt receipt = new Receipt();
                    receipt.setCode(rs.getString("code"));
                    return receipt;
                }).toList();
        return receipts;
    }
}
