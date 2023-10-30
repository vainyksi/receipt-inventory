package sk.banik.finance.receipts.inventory.service;

import org.springframework.stereotype.Service;
import sk.banik.finance.receipts.inventory.model.Receipt;
import sk.banik.finance.receipts.inventory.model.ReceiptsRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptsRepository receiptRepository;

    public ReceiptServiceImpl(ReceiptsRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public List<String> getAllReceiptCodes() {
        return StreamSupport.stream(receiptRepository.findAll().spliterator(), false)
                .map(Receipt::getCode)
                .toList();
    }

    @Override
    public String storeReceiptCode(String receiptCode) {
        Receipt receipt = new Receipt();
        receipt.setCode(receiptCode);
        Receipt receiptStored = receiptRepository.save(receipt);

        return receiptStored.getCode();
    }
}