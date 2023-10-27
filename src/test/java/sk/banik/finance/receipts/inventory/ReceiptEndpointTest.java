package sk.banik.finance.receipts.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ReceiptEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int portNumber;
    private String BASE_URL;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + portNumber + "/";
    }

    @Test
    void helloWorldTestCase() {
        String response = restTemplate.getForObject(BASE_URL + "receipt/hello-world", String.class);

        assertEquals("Hello World!", response);
    }

    @Test
    void canGetAllReceipts() {
        String response = restTemplate.getForObject(BASE_URL + "receipt/all", String.class);

        assertEquals("id1, id2, id3", response);
    }

    @Test
    void canAddNewReceipt() {
        String newReceiptId = "newReceiptId";

        String receiptIdCreated = restTemplate.postForObject(BASE_URL + "receipt/" + newReceiptId, null, String.class);
        assertEquals(newReceiptId, receiptIdCreated);

        String response = restTemplate.getForObject(BASE_URL + "receipt/all", String.class);
        assertThat(response).contains(newReceiptId);
    }
}
