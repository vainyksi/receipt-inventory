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

@Configuration
class JdbcConfiguration {
    @Bean
    public ApplicationRunner exampleOfDataReading(JdbcTemplate template){
        return args -> template
                .query("select * from receipt",
                        (rs, rowNum) -> {
                            Receipt receipt = new Receipt();
                            receipt.setId(rs.getLong("id"));
                            receipt.setCode(rs.getString("code"));
                            return receipt;
                        })
                .forEach(System.out::println);
    }
}
