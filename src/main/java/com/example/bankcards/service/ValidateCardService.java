package com.example.bankcards.service;


import com.example.bankcards.dto.EditStatusDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.ValidateCardException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class ValidateCardService {

    private final AdminServ adminServ;

    /**
     *  Метод проверки карты на срок действия.
     *  В случае просрочки изменение статуса на Expired.
     */

    public void validateExpireDate(Card card) {
        YearMonth now = YearMonth.now();
        YearMonth cardDate = YearMonth.of(card.getExpiryYear(), card.getExpiryMonth());
        if (now.isAfter(cardDate)) {
            EditStatusDto editStatusDto = EditStatusDto.builder()
                    .cardId(card.getCardId())
                    .status(Card.Status.EXPIRED)
                    .build();
            adminServ.editStatus(editStatusDto);
            throw new ValidateCardException("Карта '"+card.getLast4()+"' недействительна");
        }
    }

}
