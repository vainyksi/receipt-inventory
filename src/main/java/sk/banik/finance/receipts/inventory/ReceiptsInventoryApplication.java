package sk.banik.finance.receipts.inventory;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ReceiptsInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptsInventoryApplication.class, args);
    }
}

@Configuration
class JdbcConfiguration {
    @Bean
    public ApplicationRunner loadData(JdbcTemplate template){
        return args -> template
                .query("select * from receipts",
                        (rs, rowNum) -> new Receipt(rs.getInt("id"), rs.getString("code")))
                .forEach(System.out::println);
    }
}


record Receipt(Integer id, String code) {}