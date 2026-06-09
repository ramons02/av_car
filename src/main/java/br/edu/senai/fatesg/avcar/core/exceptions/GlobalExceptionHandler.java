package br.edu.senai.fatesg.avcar.core.exceptions;

import br.edu.senai.fatesg.avcar.core.controllers.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ApiResponse<Void>> handleNegocio(NegocioException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ApiResponse.erro(e.getMessage()));
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(EntidadeNaoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.erro(e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ApiResponse.erro(e.getMessage()));
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ErrorResponse> handleFieldValidation(FieldValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("[" + e.getField() + "] " + e.getMessage(), Severity.WARNING, 400));
    }

    @ExceptionHandler(RuleValidationException.class)
    public ResponseEntity<ErrorResponse> handleRuleValidation(RuleValidationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(new ErrorResponse(e.getMessage(), Severity.WARNING, 422));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.erro("Erro interno: " + e.getMessage()));
    }
}
