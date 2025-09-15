package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class ShowCardToAdmin {

    private long cardId;
    private String cardNumberMask;
    private BigDecimal balance;
    private Card.Status status;
    private String ownerName;
    private UserDto owner;
    private int expiryYear;
    private int expiryMonth;
}
