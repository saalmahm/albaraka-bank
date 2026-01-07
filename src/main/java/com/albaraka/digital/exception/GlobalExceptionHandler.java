package com.albaraka.digital.exception;

import com.albaraka.digital.dto.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }

    // --------- Erreurs métier ---------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse body = buildErrorResponse(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse body = buildErrorResponse(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    // --------- Validation @Valid ---------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .orElse("Requête invalide");

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse body = buildErrorResponse(status, message, request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }

    // --------- JSON / contenu requête ---------

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse body = buildErrorResponse(
                status,
                "Corps de requête invalide (JSON mal formé ou type incorrect)",
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        ErrorResponse body = buildErrorResponse(
                status,
                "Type de contenu non supporté",
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ErrorResponse body = buildErrorResponse(
                status,
                "Méthode HTTP non autorisée pour cette ressource",
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, status);
    }

    // --------- Erreurs base de données ---------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse body = buildErrorResponse(
                status,
                "Contrainte de base de données violée",
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, status);
    }

    // --------- Fallback générique ---------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse body = buildErrorResponse(
                status,
                "Une erreur interne est survenue. Veuillez réessayer plus tard.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(body, status);
    }
}