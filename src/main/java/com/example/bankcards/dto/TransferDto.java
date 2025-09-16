package com.example.bankcards.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Jacksonized
public class TransferDto {

    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
}
