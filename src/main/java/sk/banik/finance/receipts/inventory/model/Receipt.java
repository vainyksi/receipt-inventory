package sk.banik.finance.receipts.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "receipt")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String code;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", code='" + code + '\'' +
                '}';
    }
}
