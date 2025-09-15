package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@Jacksonized
public class CardDto {

    private long cardId;
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать 16 цифр")
    private String cardNumber;
    @Enumerated(EnumType.STRING)
    private Card.Status status;
    private BigDecimal balance;
    private String last4;
    private int expiryYear;
    @Min(value = 1, message = "Минимальный номер месяц = 1")
    @Max(value = 12, message = "Максимальный номер месяц = 12")
    private int expiryMonth;
    private User owner;
}
