package sk.banik.finance.receipts.inventory.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.banik.finance.receipts.inventory.rest.dto.ReceiptResponse;
import sk.banik.finance.receipts.inventory.service.ReceiptService;

import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(@Autowired ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/all")
    public List<ReceiptResponse> getAllReceipts() {
        return receiptService.getAllReceiptIds().stream().map(ReceiptResponse::new).toList();
    }

    @PostMapping("/{receiptId}")
    public String addNewReceiptId(@PathVariable String receiptId) {
        return receiptService.storeReceiptId(receiptId);
    }
}
