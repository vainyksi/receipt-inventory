package sk.banik.finance.receipts.inventory;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import sk.banik.finance.receipts.inventory.model.Receipt;
import sk.banik.finance.receipts.inventory.model.ReceiptsRepository;
import sk.banik.finance.receipts.inventory.rest.dto.ReceiptResponse;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ReceiptEndpointTest {

    @LocalServerPort
    private int portNumber;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private TestRestTemplate restTemplate;

    @MockBean
    private ReceiptsRepository receiptsRepositoryTestable;
    private final HashMap<String, Receipt> localStorage = new HashMap<>();

    @PostConstruct
    void initialize() {
        String baseUrl = "http://localhost:" + portNumber + "/";
        restTemplate = new TestRestTemplate(restTemplateBuilder.rootUri(baseUrl));

        setupLocalStorageForTesting(receiptsRepositoryTestable);
    }

    private void setupLocalStorageForTesting(ReceiptsRepository localTestableRepocitory) {
        Mockito.when(localTestableRepocitory.save(Mockito.any(Receipt.class)))
                .thenAnswer(invocation -> {
                    Receipt receipt = invocation.getArgument(0);
                    localStorage.put(receipt.getCode(), receipt);
                    return receipt;
                });
    }

    @Test
    void shouldReturnEmptyListOfReceiptsFirst() {
        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);

        assertThat(allReceipts.getBody()).isEmpty();
    }

    @Test
    void shouldReturnAllReceiptsAdded() {
        restTemplate.postForObject("/receipt/" + "id1", null, String.class);
        restTemplate.postForObject("/receipt/" + "id2", null, String.class);
        restTemplate.postForObject("/receipt/" + "id3", null, String.class);

        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);

        assertThat(allReceipts.getBody()).contains(
                new ReceiptResponse("id1"),
                new ReceiptResponse("id2"),
                new ReceiptResponse("id3"));
    }

    @Test
    void canAddNewReceipt() {
        String newReceiptId = "newReceiptId";

        ResponseEntity<ReceiptResponse[]> allReceiptsBeforeInsertion =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);
        assertThat(allReceiptsBeforeInsertion.getBody()).doesNotContain(new ReceiptResponse(newReceiptId));

        String receiptIdCreated = restTemplate.postForObject("/receipt/" + newReceiptId, null, String.class);
        assertEquals(newReceiptId, receiptIdCreated);

        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);
        assertThat(allReceipts.getBody()).contains(new ReceiptResponse(newReceiptId));
    }
}
