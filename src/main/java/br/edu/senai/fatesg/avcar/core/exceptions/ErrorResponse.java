package br.edu.senai.fatesg.avcar.core.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private String message;
    private Severity severity;
    private LocalDateTime timestamp;
    private int status;

    public ErrorResponse(String message, Severity severity, int status) {
        this.message = message;
        this.severity = severity;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
