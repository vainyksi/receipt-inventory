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
import org.springframework.http.ResponseEntity;
import sk.banik.finance.receipts.inventory.rest.dto.ReceiptResponse;

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
    void shouldReturnEmptyListOfReceiptsFirst() {
        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity(BASE_URL + "receipt/all", ReceiptResponse[].class);

        assertThat(allReceipts.getBody()).isEmpty();
    }

    @Test
    void shouldReturnAllReceiptsAdded() {
        restTemplate.postForObject(BASE_URL + "receipt/" + "id1", null, String.class);
        restTemplate.postForObject(BASE_URL + "receipt/" + "id2", null, String.class);
        restTemplate.postForObject(BASE_URL + "receipt/" + "id3", null, String.class);

        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity(BASE_URL + "receipt/all", ReceiptResponse[].class);

        assertThat(allReceipts.getBody()).contains(
                new ReceiptResponse("id1"),
                new ReceiptResponse("id2"),
                new ReceiptResponse("id3"));
    }

    @Test
    void canAddNewReceipt() {
        String newReceiptId = "newReceiptId";

        ResponseEntity<ReceiptResponse[]> allReceiptsBeforeInsertion =
                restTemplate.getForEntity(BASE_URL + "receipt/all", ReceiptResponse[].class);
        assertThat(allReceiptsBeforeInsertion.getBody()).doesNotContain(new ReceiptResponse(newReceiptId));

        String receiptIdCreated = restTemplate.postForObject(BASE_URL + "receipt/" + newReceiptId, null, String.class);
        assertEquals(newReceiptId, receiptIdCreated);

        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity(BASE_URL + "receipt/all", ReceiptResponse[].class);
        assertThat(allReceipts.getBody()).contains(new ReceiptResponse(newReceiptId));
    }
}
