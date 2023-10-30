package sk.banik.finance.receipts.inventory.service;

import java.util.List;

public interface ReceiptService {
    List<String> getAllReceiptCodes();

    String storeReceiptCode(String receiptCode);
}
