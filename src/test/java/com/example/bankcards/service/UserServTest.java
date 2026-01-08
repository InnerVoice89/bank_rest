package com.example.bankcards.service;


import com.example.bankcards.dto.ShowCardDto;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.UserDetailsImpl;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardsRepository;
import com.example.bankcards.repository.HistoryRepository;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class UserServTest {

//    Пришлось изолировать моки из-за их конфликтов,добавив в каждый тест

//    @Mock
//    private CardsRepository cardsRepository;
//    @Mock
//    private HistoryRepository historyRepository;
//    @InjectMocks
//    private UserServ userServ;
//    @Mock
//    private ValidateCardService validateCardService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        when(userDetails.getId()).thenReturn(1L);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void testGetCardById() {
        CardsRepository cardsRepository = mock(CardsRepository.class);
        HistoryRepository historyRepository = mock(HistoryRepository.class);
        ValidateCardService validateCardService = mock(ValidateCardService.class);
        UserServ userServ = new UserServ(cardsRepository, historyRepository, validateCardService);

        User owner = User.builder().name("Ivan").surname("Sidorov").build();
        Card card = new Card();
        card.setOwner(owner);
        card.setExpiryMonth(1);
        card.setBalance(new BigDecimal("500"));
        when(cardsRepository.findByCardIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(card));
        ShowCardDto dto = userServ.getCardById(1L);
        assertEquals(dto.getOwnerName(), "Sidorov I");
        assertTrue(dto.getBalance().compareTo(BigDecimal.valueOf(500)) == 0);
    }

    //Позитивный сценарий перевода на карту другого пользователя
    @Test
    public void transferToOtherCardsTest() {
        CardsRepository cardsRepository = mock(CardsRepository.class);
        HistoryRepository historyRepository = mock(HistoryRepository.class);
        ValidateCardService validateCardService = mock(ValidateCardService.class);

        UserServ userServ = new UserServ(cardsRepository, historyRepository, validateCardService);
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransferDto transfer = TransferDto.builder()
                .fromCardId(1L).toCardId(22L).amount(amount).build();
        User ownerFrom = User.builder().id(1L).build();
        User ownerTo = User.builder().id(2L).build();
        Card cardFrom = Card.builder().cardId(1L).owner(ownerFrom).balance(BigDecimal.valueOf(500.00))
                .status(Card.Status.ACTIVE).build();
        Card cardTo = Card.builder().cardId(2L).owner(ownerTo).balance(BigDecimal.valueOf(200))
                .status(Card.Status.ACTIVE).build();

        when(cardsRepository.findByCardIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(cardFrom));
        when(cardsRepository.findByCardId(anyLong())).thenReturn(Optional.of(cardTo));
        doNothing().when(validateCardService).validateExpireDate(cardTo);
        doNothing().when(validateCardService).validateExpireDate(cardFrom);

        BigDecimal newBalance = userServ.makeTransferToOtherCard(transfer);
        assertTrue(newBalance.compareTo(BigDecimal.valueOf(400)) == 0);
        assertTrue(cardTo.getBalance().compareTo(BigDecimal.valueOf(300)) == 0);
    }

    //Попытка перевести на свою карту
    @Test(expectedExceptions = RuntimeException.class)
    public void transferToOtherCardsExTest() {
        CardsRepository cardsRepository = mock(CardsRepository.class);
        HistoryRepository historyRepository = mock(HistoryRepository.class);
        ValidateCardService validateCardService = mock(ValidateCardService.class);

        UserServ userServ = new UserServ(cardsRepository, historyRepository, validateCardService);
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransferDto transfer = TransferDto.builder()
                .fromCardId(1L).toCardId(22L).amount(amount).build();
        User ownerFrom = User.builder().id(1L).build();
        User ownerTo = User.builder().id(1L).build();
        Card cardFrom = Card.builder().cardId(1L).owner(ownerFrom).balance(BigDecimal.valueOf(500.00))
                .status(Card.Status.ACTIVE).build();
        Card cardTo = Card.builder().cardId(2L).owner(ownerTo).balance(BigDecimal.valueOf(200))
                .status(Card.Status.ACTIVE).build();

        when(cardsRepository.findByCardIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(cardFrom));
        when(cardsRepository.findByCardId(anyLong())).thenReturn(Optional.of(cardTo));
        doNothing().when(validateCardService).validateExpireDate(cardTo);
        doNothing().when(validateCardService).validateExpireDate(cardFrom);
        userServ.makeTransferToOtherCard(transfer);
    }

}
