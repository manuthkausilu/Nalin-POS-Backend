package com.shashi.possysytembackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false, length = 255)
    private String customerName;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(unique = true, length = 255)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;
}
