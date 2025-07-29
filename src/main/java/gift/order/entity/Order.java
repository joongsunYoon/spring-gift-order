package gift.order.entity;

import gift.product.entity.Option;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    private String message;
    private LocalDateTime orderDateTime;
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id" , nullable = false)
    private Option option;

    protected Order() {}
    public Order(Long quantity, String message, Option option, LocalDateTime orderDateTime, Long memberId) {
        this.quantity = quantity;
        this.message = message;
        this.option = option;
        this.orderDateTime = orderDateTime;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Option getOption() {
        return option;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public Long getMemberId() {
        return memberId;
    }
}
