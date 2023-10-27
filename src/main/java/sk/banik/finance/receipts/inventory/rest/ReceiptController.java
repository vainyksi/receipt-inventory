package sk.banik.finance.receipts.inventory.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private String listOfReceiptIds = "id1, id2, id3";

    @GetMapping("/hello-world")
    public String getHelloWorld() {
        return "Hello World!";
    }

    @GetMapping("/all")
    public String getAllReceipts() {
        return listOfReceiptIds;
    }

    @PostMapping("/{receiptId}")
    public String addNewReceiptId(@PathVariable String receiptId) {
        listOfReceiptIds += ", " + receiptId;

        return receiptId;
    }
}
