package sk.banik.finance.receipts.inventory.rest;

import org.springframework.web.bind.annotation.*;
import sk.banik.finance.receipts.inventory.model.Receipt;
import sk.banik.finance.receipts.inventory.model.ReceiptsRepository;
import sk.banik.finance.receipts.inventory.rest.dto.ReceiptResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final List<String> listOfReceiptIds;

    private final ReceiptsRepository receiptRepository;

    public ReceiptController(ReceiptsRepository receiptRepository) {
        listOfReceiptIds = new ArrayList<>();
        this.receiptRepository = receiptRepository;
    }

    @GetMapping("/all")
    public List<ReceiptResponse> getAllReceipts() {
        return listOfReceiptIds.stream().map(ReceiptResponse::new).toList();
    }

    @PostMapping("/{receiptId}")
    public String addNewReceiptId(@PathVariable String receiptId) {
        listOfReceiptIds.add(receiptId);

        Receipt receipt = new Receipt();
        receipt.setCode(receiptId);
        Receipt receiptStored = receiptRepository.save(receipt);

        return receiptStored.getCode();
    }
}
