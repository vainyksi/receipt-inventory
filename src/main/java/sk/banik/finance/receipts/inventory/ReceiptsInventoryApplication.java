package sk.banik.finance.receipts.inventory;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.banik.finance.receipts.inventory.model.Receipt;

@SpringBootApplication
public class ReceiptsInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptsInventoryApplication.class, args);
    }

}
