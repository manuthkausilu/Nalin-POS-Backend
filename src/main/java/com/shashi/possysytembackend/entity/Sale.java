package com.shashi.possysytembackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDiscount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> saleItems;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal itemDiscounts;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderDiscountPercentage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderDiscount;

    @Column( precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Column( precision = 10, scale = 2)
    private BigDecimal balance;

    @PrePersist
    public void prePersist() {
        saleDate = LocalDateTime.now();
    }

    public enum PaymentMethod {
        CASH, CARD,OTHER
    }
}
