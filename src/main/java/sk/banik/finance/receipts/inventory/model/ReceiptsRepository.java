package sk.banik.finance.receipts.inventory.model;

import org.springframework.data.repository.CrudRepository;

public interface ReceiptsRepository extends CrudRepository<Receipt, Long> {
}
