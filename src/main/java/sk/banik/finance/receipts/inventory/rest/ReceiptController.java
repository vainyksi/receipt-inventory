package sk.banik.finance.receipts.inventory.rest;

import org.springframework.web.bind.annotation.*;
import sk.banik.finance.receipts.inventory.rest.dto.ReceiptResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final List<String> listOfReceiptIds;

    public ReceiptController() {
        listOfReceiptIds = new ArrayList<>(List.of("id1", "id2", "id3"));
    }

    @GetMapping("/all")
    public List<ReceiptResponse> getAllReceipts() {
        return listOfReceiptIds.stream().map(ReceiptResponse::new).toList();
    }

    @PostMapping("/{receiptId}")
    public String addNewReceiptId(@PathVariable String receiptId) {
        listOfReceiptIds.add(receiptId);

        return receiptId;
    }
}
