package sk.banik.finance.receipts.inventory.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "receipt")
public class Receipt {

    @Id
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "code='" + code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Objects.equals(code, receipt.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
