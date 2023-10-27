package sk.banik.finance.receipts.inventory.service;

import org.springframework.stereotype.Service;
import sk.banik.finance.receipts.inventory.model.Receipt;
import sk.banik.finance.receipts.inventory.model.ReceiptsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptsRepository receiptRepository;
    private final List<String> listOfReceiptIds = new ArrayList<>();

    public ReceiptServiceImpl(ReceiptsRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public List<String> getAllReceiptIds() {
        return listOfReceiptIds;
    }

    @Override
    public String storeReceiptId(String receiptId) {
        Receipt receipt = new Receipt();
        receipt.setCode(receiptId);
        Receipt receiptStored = receiptRepository.save(receipt);

        listOfReceiptIds.add(receiptId);
        return receiptStored.getCode();
    }
}