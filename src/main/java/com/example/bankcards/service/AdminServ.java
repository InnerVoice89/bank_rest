package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.History;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardsRepository;
import com.example.bankcards.repository.HistoryRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import com.example.bankcards.util.EncryptionUtils;
import com.example.bankcards.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServ {
    private final CardsRepository cardsRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionUtils encryptionUtils;
    private final HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public List<ShowCardToAdmin> getCardsByUserId(Long id) {
        List<Card> cards = cardsRepository.findByOwnerId(id);
        return cards.stream().map(CardMapper::cardToAdminCard).toList();
    }

    @Transactional(readOnly = true)
    public ShowCardToAdmin getCardById(long id) {
        Card card = cardsRepository.findByCardId(id).orElseThrow(() -> new CardNotFoundException("Карта не найдена"));
        return CardMapper.cardToAdminCard(card);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ShowCardToAdmin> getAllCards() {
        return cardsRepository.findAll().stream().map(CardMapper::cardToAdminCard).toList();
    }


    @Transactional
    public void editStatus(EditStatusDto editStatus) {
        Card editCard = cardsRepository.findByCardId(editStatus.getCardId()).orElseThrow(
                () -> new RuntimeException("Карта не найдена"));
        editCard.setStatus(editStatus.getStatus());
    }

    @Transactional
    public String deleteCardById(long id) {
        Card deletedCard = cardsRepository.findByCardId(id).orElseThrow(() -> new RuntimeException("Карта не найдена"));
        cardsRepository.delete(deletedCard);
        return deletedCard.getLast4();
    }

    @Transactional
    public String deleteUser(long id) {
        User deletedUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        userRepository.delete(deletedUser);
        return deletedUser.getUsername();
    }

    @Transactional
    public void addNewCard(CardDto card) throws NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (card.getOwner() == null) {
            throw new RuntimeException("Карта должна быть привязана к пользователю");
        }
        card.setLast4(card.getCardNumber().substring(card.getCardNumber().length() - 4));
        userRepository.findById(card.getOwner().getId()).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден"));
        card.setCardNumber(encryptionUtils.encrypt(card.getCardNumber()));
        cardsRepository.save(CardMapper.toEntity(card));
    }

    @Transactional
    public void addNewUser(UserDto user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(UserMapper.toEntity(user));
        } else {
            throw new RuntimeException("Отсутствует пароль");
        }
    }

    @Transactional
    public void addRole(Role role, long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (user.getRoles().contains(role)) {
            throw new RuntimeException("Такая роль у данного пользователя уже есть");
        }
        user.getRoles().add(role);
    }

    @Transactional
    public void removeRole(Role role, long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (!user.getRoles().contains(role)) {
            throw new RuntimeException("Такой роли у данного пользователя нет");
        }
        user.getRoles().remove(role);
    }

    public List<History> showHistory() {
        return historyRepository.findAll();
    }
}
