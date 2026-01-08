package com.example.bankcards.controller;


import com.example.bankcards.dto.*;
import com.example.bankcards.entity.History;
import com.example.bankcards.service.AdminServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(
        name = "Контроллер для работы с сервисами администратора"
)
public class AdminController {
    private final AdminServ adminServ;

    @GetMapping("/get-cards-by-user-id/{id}")
    @Operation(
            summary = "Сервис для получения карт по Id пользователя"
    )
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
    @Operation(
            summary = "Сервис для получения карт по Id карты"
    )
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
    @Operation(
            summary = "Сервис для получения всех карт"
    )
    public List<ShowCardToAdmin> getAllCards() {
        return adminServ.getAllCards();
    }

    @GetMapping("all-users")
    public List<UserDto> allUsers() {
        return adminServ.getAllUsers();
    }


    @PostMapping("/edit-status")
    @Operation(
            summary = "Сервис для редактирования статуса карты"
    )
    public BaseResponse editStatus(@RequestBody EditStatusDto editStatus) {
        try {
            adminServ.editStatus(editStatus);
            return BaseResponse.builder()
                    .success(true)
                    .message("Статус успешно изменён")
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Проблема с изменением статуса : "+e.getMessage())
                    .build();
        }

    }

    @GetMapping("/delete-card/{id}")
    @Operation(
            summary = "Сервис для удаления карты"
    )
    public BaseResponse deleteCardById(@PathVariable long id) {
        try {
            String cardMask = adminServ.deleteCardById(id);
            return BaseResponse.builder()
                    .success(true)
                    .message("Карта : " + cardMask+" успешно удалена")
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Проблема с удалением карты : "+e.getMessage())
                    .build();
        }
    }

    @GetMapping("/delete-user/{id}")
    @Operation(
            summary = "Сервис для удаления пользователя"
    )
    public BaseResponse deleteUser(@PathVariable long id) {
        try {
            String username = adminServ.deleteUser(id);
            return BaseResponse.builder()
                    .success(true)
                    .message("Пользователь удален : " + username)
                    .build();

        } catch (Exception e) {
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Проблема с удалением пользователя : "+e.getMessage())
                    .build();
        }
    }

    @PostMapping("/create-card")
    @Operation(
            summary = "Сервис для создания карты"
    )
    public BaseResponse addNewCard(@Valid @RequestBody CardDto card) {
        try {
            adminServ.addNewCard(card);
            return BaseResponse.builder()
                    .success(true)
                    .message("Карта успешно сохранена")
                    .build();
        } catch (Exception e) {
            log.error("Ошибка добавления карты ", e);
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Ошибка добавления карты : "+e.getMessage())
                    .build();
        }
    }

    @PostMapping("/create-user")
    @Operation(
            summary = "Сервис для создания пользователя"
    )
    public BaseResponse addNewUser(@Valid @RequestBody UserDto user) {
        try {
            adminServ.addNewUser(user);
            return BaseResponse.builder()
                    .success(true)
                    .message("Пользователь сохранен")
                    .build();
        } catch (Exception e) {
            log.error("Ошибка добавления пользователя ", e);
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Ошибка добавления пользователя : "+e.getMessage())
                    .build();
        }
    }

    @PostMapping("/add-role")
    @Operation(
            summary = "Сервис для добавления роли "
    )
    public BaseResponse addRole(@RequestBody RoleDto newRole) {
        try {
            adminServ.addRole(newRole.getRole(), newRole.getId());
            return BaseResponse.builder()
                    .success(true)
                    .message("Роль добавлена")
                    .build();
        } catch (Exception e) {
            log.error("Ошибка при добавлении роли ", e);
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Ошибка при добавлении роли : "+e.getMessage())
                    .build();
        }
    }

    @PostMapping("/remove-role")
    @Operation(
            summary = "Сервис для удаления роли"
    )
    public BaseResponse RemoveRole(@RequestBody RoleDto role) {
        try {
            adminServ.removeRole(role.getRole(), role.getId());
            return BaseResponse.builder()
                    .success(true)
                    .message("Роль удалена")
                    .build();
        } catch (Exception e) {
            log.error("Ошибка при удалении роли ", e);
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Ошибка при удалении роли : "+e.getMessage())
                    .build();
        }
    }

    @GetMapping("/show-history")
    @Operation(
            summary = "Сервис для просмотра истории работ с картами"
    )
    @SecurityRequirement(name = "bearerAuth")
    public List<History> showHistory() {
        return adminServ.showHistory();
    }

}

