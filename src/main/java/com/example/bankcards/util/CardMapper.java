package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.ShowCardDto;
import com.example.bankcards.dto.ShowCardToAdmin;
import com.example.bankcards.entity.Card;

import java.time.YearMonth;

public class CardMapper {

    public static CardDto toCardDto(Card card) {
        return CardDto.builder()
                .owner(card.getOwner())
                .balance(card.getBalance())
                .status(card.getStatus())
                .expiryYear(card.getExpiryYear())
                .expiryMonth(card.getExpiryMonth())
                .last4(card.getLast4())
                .build();
    }

    public static Card toEntity(CardDto cardDto) {
        return Card.builder()
                .cardNumber(cardDto.getCardNumber())
                .owner(cardDto.getOwner())
                .balance(cardDto.getBalance())
                .status(cardDto.getStatus())
                .expiryYear(cardDto.getExpiryYear())
                .expiryMonth(cardDto.getExpiryMonth())
                .last4(cardDto.getLast4())
                .build();
    }

    public static ShowCardDto cardToShowCard(Card card) {
            return ShowCardDto.builder()
                    .cardNumberMask(makeMaskToCard(card.getLast4()))
                    .ownerName(card.getOwner().getSurname() + " " + card.getOwner().getName().charAt(0))
                    .balance(card.getBalance())
                    .status(card.getStatus())
                    .expirationDate(YearMonth.of(card.getExpiryYear(), card.getExpiryMonth()))
                    .build();

    }

    public static ShowCardToAdmin cardToAdminCard(Card card) {
        return ShowCardToAdmin.builder()
                .cardId(card.getCardId())
                .cardNumberMask(makeMaskToCard(card.getLast4()))
                .ownerName(card.getOwner().getSurname() + " " + card.getOwner().getName().charAt(0))
                .owner(UserMapper.toDto(card.getOwner()))
                .balance(card.getBalance())
                .status(card.getStatus())
                .expiryYear(card.getExpiryYear())
                .expiryMonth(card.getExpiryMonth())
                .build();
    }

    public static String makeMaskToCard(String last4) {
        return "**** **** **** " + last4;
    }

}
