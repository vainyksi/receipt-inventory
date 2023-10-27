package sk.banik.finance.receipts.inventory.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    @GetMapping("/hello-world")
    public String getHelloWorld() {
        return "Hello World!";
    }

    @GetMapping("/all")
    public String getAllReceipts() {
        return "id1, id2, id3";
    }
}
