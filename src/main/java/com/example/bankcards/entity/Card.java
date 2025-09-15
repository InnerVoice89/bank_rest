package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Entity(name = "cards")
@Getter
@Setter
@ToString
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardId;

    private String cardNumber;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Builder.Default
    private BigDecimal balance=BigDecimal.ZERO;
    private String last4;
    private int expiryYear;
    private int expiryMonth;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;


   public enum Status {
        ACTIVE,
        BLOCKED,
        EXPIRED

    }
}
