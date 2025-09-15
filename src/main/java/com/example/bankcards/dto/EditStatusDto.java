package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditStatusDto {

    private long cardId;
    @Enumerated(EnumType.STRING)
    private Card.Status status;
}
