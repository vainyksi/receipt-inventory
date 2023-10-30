package sk.banik.finance.receipts.inventory.rest;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
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
        Mockito.when(localTestableRepocitory.findAll())
                .thenReturn(localStorage.values());
    }

    @AfterEach
    void tearDown() {
        localStorage.clear();
    }

    @Test
    void shouldReturnEmptyListOfReceiptsFirst() {
        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);

        assertThat(allReceipts.getBody()).isEmpty();
    }

    @Test
    void shouldReturnAllReceiptsAdded() {
        restTemplate.postForObject("/receipt/" + "code1", null, String.class);
        restTemplate.postForObject("/receipt/" + "code2", null, String.class);
        restTemplate.postForObject("/receipt/" + "code3", null, String.class);

        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);

        assertThat(allReceipts.getBody()).contains(
                new ReceiptResponse("code1"),
                new ReceiptResponse("code2"),
                new ReceiptResponse("code3"));
    }

    @Test
    void canAddNewReceipt() {
        String newReceiptCode = "newReceiptCode";

        ResponseEntity<ReceiptResponse[]> allReceiptsBeforeInsertion =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);
        assertThat(allReceiptsBeforeInsertion.getBody()).doesNotContain(new ReceiptResponse(newReceiptCode));

        String receiptCodeStored = restTemplate.postForObject("/receipt/" + newReceiptCode, null, String.class);
        assertEquals(newReceiptCode, receiptCodeStored);

        ResponseEntity<ReceiptResponse[]> allReceipts =
                restTemplate.getForEntity("/receipt/all", ReceiptResponse[].class);
        assertThat(allReceipts.getBody()).contains(new ReceiptResponse(newReceiptCode));
    }
}
