package com.albaraka.digital.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;    // ex: "Bad Request", "Unauthorized"
    private String message;  // message métier lisible
    private String path;
}