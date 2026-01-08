package com.example.bankcards.controller;

import com.example.bankcards.dto.BaseResponse;
import com.example.bankcards.dto.ErrorResponse;
import com.example.bankcards.dto.ShowCardDto;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.service.UserServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(
        name = "Контроллер для работы с сервисами пользователей"
)
public class UserController {

    private final UserServ userServ;

    @GetMapping("/show")
    @Operation(
            summary = "Сервис для получения информации по картам пользователя"
    )
    public List<ShowCardDto> getCards() {
        return userServ.getAllCards();
    }

    @GetMapping("/show-by-id/{id}")
    @Operation(
            summary = "Сервис для получения информации по определенной карте пользователя"
    )
    public ResponseEntity<?> getCardById(@PathVariable long id) {
        try {
            ShowCardDto cardDto = userServ.getCardById(id);
            return ResponseEntity.ok(cardDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                    "Ошибка получения карты : " + e.getMessage()
                    , LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/transfer-yours")
    @Operation(
            summary = "Сервис для перевода между своими картами"
    )
    public BaseResponse transferBetweenCards(@RequestBody TransferDto transferData) {
        try {
            userServ.makeTransferBetweenYourCards(transferData);
            return BaseResponse.builder()
                    .success(true)
                    .message("Средства успешно переведены")
                    .build();
        } catch (Exception e) {
            log.error("Ошибка перевода ", e);
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Ошибка перевода : " + e.getMessage())
                    .build();
        }
    }

    @PostMapping("/transfer-other")
    @Operation(
            summary = "Сервис для перевода на карту другого пользователя"
    )
    public BaseResponse transferToOtherCard(@RequestBody TransferDto transferData) {
        try {
            var newBalance = userServ.makeTransferToOtherCard(transferData);
            return BaseResponse.builder()
                    .success(true)
                    .message("Деньги переведены успешно,Ваш баланс составляет : " + newBalance)
                    .build();
        } catch (Exception e) {
            log.error("Ошибка перевода ", e);
            return BaseResponse.builder()
                    .success(false)
                    .errorMessage("Ошибка перевода! " + e.getMessage())
                    .build();
        }
    }


}
