package com.cibertec.experienciasinformativasV.excepcion;

import com.cibertec.experienciasinformativasV.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> manejarRuntimeException(RuntimeException ex) {

        String mensaje = ex.getMessage();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (mensaje != null && (
                mensaje.contains("reservado") ||
                mensaje.contains("horario") ||
                mensaje.contains("correo ya está registrado") ||
                mensaje.contains("Usuario inactivo")
        )) {
            status = HttpStatus.CONFLICT;
        }

        ErrorResponse error = new ErrorResponse(mensaje, status.value());

        return ResponseEntity.status(status).body(error);
    }
}