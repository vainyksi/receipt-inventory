package sk.banik.finance.receipts.inventory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ReceiptEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int portNumber;

    @Test
    void helloWorldTestCase() {
        String response = restTemplate.getForObject("http://localhost:" + portNumber + "/" +
                        "receipt/hello-world",
                String.class);


        Assertions.assertEquals("Hello World!", response);
    }

    @Test
    void canGetAllReceipts() {
        String response = restTemplate.getForObject("http://localhost:" + portNumber + "/" +
                        "receipt/all",
                String.class);

        Assertions.assertEquals("id1, id2, id3", response);
    }

    @Test
    void canAddNewReceipt() {
        String newReceiptId = "newReceiptId";

        String receiptIdCreated = restTemplate.postForObject("http://localhost:" + portNumber + "/" +
                        "receipt/" + newReceiptId,
                null, String.class);
        Assertions.assertEquals(newReceiptId, receiptIdCreated);

        String response = restTemplate.getForObject("http://localhost:" + portNumber + "/" +
                        "receipt/all",
                String.class);
        assertThat(response).contains(newReceiptId);
    }
}
