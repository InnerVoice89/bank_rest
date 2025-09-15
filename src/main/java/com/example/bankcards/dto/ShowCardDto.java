package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;

@Builder
@Getter
@Setter
public class ShowCardDto {

    private String cardNumberMask;
    private BigDecimal balance;
    private Card.Status status;
    private String ownerName;
    private YearMonth expirationDate;




}
