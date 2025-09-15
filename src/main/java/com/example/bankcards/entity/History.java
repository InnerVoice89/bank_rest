package com.example.bankcards.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String transferFromCard;
    private String transferFromUsername;
    private LocalDateTime creationDate;
    private String transferToCard;
    private String transferToUsername;
    private BigDecimal amount;




}