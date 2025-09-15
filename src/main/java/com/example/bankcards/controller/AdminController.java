package com.example.bankcards.controller;


import com.example.bankcards.dto.*;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.History;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardsRepository;
import com.example.bankcards.service.AdminServ;
import com.example.bankcards.service.UserServ;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServ adminServ;

    @GetMapping("/get-cards-by-user-id/{id}")
    public ResponseEntity<List<ShowCardToAdmin>> getCardByUserId(@PathVariable Long id) {
        try {
            List<ShowCardToAdmin> cards = adminServ.getCardsByUserId(id);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            log.error("Не удалось получить карты по id : {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-cards-by-card-id/{id}")
    public ResponseEntity<ShowCardToAdmin> getCardById(@PathVariable Long id) {
        try {
            ShowCardToAdmin card = adminServ.getCardById(id);
            return ResponseEntity.ok(card);
        } catch (Exception e) {
            log.error("Проблема с получением карты : {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all-cards")
    public List<ShowCardToAdmin> getAllCards() {
        return adminServ.getAllCards();
    }

    @GetMapping("all-users")
    public List<UserDto> allUsers() {
        return adminServ.getAllUsers();
    }

    @PostMapping("/edit-status")
    public ResponseEntity<String> editStatus(@RequestBody EditStatusDto editStatus) {
        try {
            adminServ.editStatus(editStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Статус успешно изменён");
    }

    @PostMapping("/delete-card/{id}")
    public ResponseEntity<String> deleteCardById(@PathVariable long id) {
        try {
            String cardMask = adminServ.deleteCardById(id);
            return ResponseEntity.ok("Карта удалена : " + cardMask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        try {
            String username = adminServ.deleteUser(id);
            return ResponseEntity.ok("Пользователь удален : " + username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create-card")
    public ResponseEntity<String> addNewCard(@Valid @RequestBody CardDto card) {
        try {
            adminServ.addNewCard(card);
            return ResponseEntity.ok("Карта успешно сохранена");
        } catch (Exception e) {
            log.error("Ошибка добавления карты ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody UserDto user) {
        try {
            adminServ.addNewUser(user);
            return ResponseEntity.ok("Пользователь сохранен");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add-role")
    public ResponseEntity<String> addRole(@RequestBody RoleDto newRole) {
        try {
            adminServ.addRole(newRole.getRole(), newRole.getId());
            return ResponseEntity.ok("Роль добавлена");
        } catch (Exception e) {
            log.error("Ошибка при добавлении роли ", e);
            return ResponseEntity.badRequest().body("Возникла ошибка при добавлении роли");
        }
    }

    @PostMapping("/remove-role")
    public ResponseEntity<String> RemoveRole(@RequestBody RoleDto role) {
        try {
            adminServ.removeRole(role.getRole(), role.getId());
            return ResponseEntity.ok("Роль удалена");
        } catch (Exception e) {
            log.error("Ошибка при удалении роли ", e);
            return ResponseEntity.badRequest().body("Возникла ошибка при удалении роли");
        }
    }

    @GetMapping("/show-history")
    public List<History> showHistory() {
        return adminServ.showHistory();
    }

}

