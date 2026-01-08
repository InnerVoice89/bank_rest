package com.example.bankcards.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class BaseResponse {
    private boolean success;
    private String message;
    private String errorMessage;

}
