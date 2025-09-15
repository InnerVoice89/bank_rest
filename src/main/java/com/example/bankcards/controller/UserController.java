package com.example.bankcards.controller;

import com.example.bankcards.dto.ShowCardDto;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.service.UserServ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServ userServ;

    @GetMapping("/show")
    public List<ShowCardDto> getCards() {
        return userServ.getAllCards();
    }

    @GetMapping("/show-by-id/{id}")
    public ShowCardDto getCardById(@PathVariable long id) {
        try {
            return userServ.getCardById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/transfer-yours")
    public ResponseEntity<String> transferBetweenCards(@RequestBody TransferDto transferData) {
        try {
            userServ.makeTransferBetweenYourCards(transferData);
            return ResponseEntity.ok("Деньги переведены успешно");
        }catch(Exception e){
            log.error("Ошибка перевода ",e);
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transfer-other")
    public ResponseEntity<String> transferToOtherCard(@RequestBody TransferDto transferData) {
        try {
           var newBalance= userServ.makeTransferToOtherCard(transferData);
            return ResponseEntity.ok("Деньги переведены успешно,Ваш баланс составляет : " +newBalance);
        }catch(Exception e){
            log.error("Ошибка перевода ",e);
            return ResponseEntity.badRequest().body("Ошибка перевода! "+e.getMessage());
        }
    }


}
