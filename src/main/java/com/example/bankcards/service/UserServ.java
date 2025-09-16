package com.example.bankcards.service;

import com.example.bankcards.dto.ShowCardDto;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.UserDetailsImpl;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.History;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardsRepository;
import com.example.bankcards.repository.HistoryRepository;
import com.example.bankcards.util.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServ {

    private final CardsRepository cardsRepository;
    private final HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public List<ShowCardDto> getAllCards() {
        List<Card> cards = cardsRepository.findByOwnerId(getUserDetails().getId());
        return cards.stream().map(CardMapper::cardToShowCard).toList();
    }

    @Transactional(readOnly = true)
    public ShowCardDto getCardById(long id) {
        Card card = cardsRepository.findByCardIdAndOwnerId(id, getUserDetails().getId()).orElseThrow(() ->
                new CardNotFoundException("Карта не найдена"));
        return CardMapper.cardToShowCard(card);
    }

    @Transactional
    public void makeTransferBetweenYourCards(TransferDto transferDto) {
        Card cardFrom = cardsRepository.findByCardIdAndOwnerId(transferDto.getFromCardId(), getUserDetails().getId())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена"));
        Card cardTo = cardsRepository.findByCardIdAndOwnerId(transferDto.getToCardId(), getUserDetails().getId())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена"));
        History createHistory = newHistory(cardFrom.getLast4(), cardFrom.getOwner().getUsername(), cardTo.getLast4(),
                cardTo.getOwner().getUsername(), transferDto.getAmount());
        historyRepository.save(createHistory);
        BigDecimal newBalance = validateAndCalculateNewBalance(transferDto.getAmount(), cardFrom, cardTo);
        cardFrom.setBalance(newBalance);
        cardTo.setBalance(cardTo.getBalance().add(transferDto.getAmount()).setScale(2, RoundingMode.HALF_UP));

    }

    @Transactional
    public BigDecimal makeTransferToOtherCard(TransferDto transferDto) {
        Card cardFrom = cardsRepository.findByCardIdAndOwnerId(transferDto.getFromCardId(), getUserDetails().getId())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена"));
        Card cardTo = cardsRepository.findByCardId(transferDto.getToCardId())
                .orElseThrow(() -> new CardNotFoundException("Карта получателя не найдена"));
        if (cardTo.getOwner().getId() == getUserDetails().getId()) {
            throw new RuntimeException("Перевод на свою карту недопустим");
        }
        BigDecimal newBalance = validateAndCalculateNewBalance(transferDto.getAmount(), cardFrom, cardTo);
        cardFrom.setBalance(newBalance);
        cardTo.setBalance(cardTo.getBalance().add(transferDto.getAmount()).setScale(2, RoundingMode.HALF_UP));
        History createHistory = newHistory(cardFrom.getLast4(), cardFrom.getOwner().getUsername(), cardTo.getLast4(),
                cardTo.getOwner().getUsername(), transferDto.getAmount());
        historyRepository.save(createHistory);
        return newBalance;
    }

    public static BigDecimal validateAndCalculateNewBalance(BigDecimal amount, Card cardFrom, Card cardTo) {
        BigDecimal newBalance = cardFrom.getBalance().subtract(amount).setScale(2,
                RoundingMode.HALF_UP);
        if (cardFrom.getStatus() != Card.Status.ACTIVE) {
            throw new RuntimeException("Карта : " + CardMapper.makeMaskToCard(cardFrom.getLast4()) + " неактивна");
        } else if (cardTo.getStatus() != Card.Status.ACTIVE) {
            throw new RuntimeException("Карта : " + CardMapper.makeMaskToCard(cardTo.getLast4()) + " неактивна");
        } else if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Недостаточно денег на карте");
        }
        return newBalance;
    }

    public UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public History newHistory(String transferFromCard, String transferFromUsername, String transferToCard,
                              String transferToUsername, BigDecimal amount) {
        return History.builder()
                .transferFromCard(transferFromCard)
                .transferFromUsername(transferFromUsername)
                .transferToCard(transferToCard)
                .transferToUsername(transferToUsername)
                .creationDate(LocalDateTime.now())
                .amount(amount).build();
    }

}
