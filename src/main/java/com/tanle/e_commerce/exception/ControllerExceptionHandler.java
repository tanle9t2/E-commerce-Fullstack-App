package com.tanle.e_commerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(ResourceNotFoundExeption exeption) {
        ExceptionResponse response = ExceptionResponse.builder()
                .type("/exception/"+ exeption.getClass().getSimpleName())
                .title("Resource not found")
                .detail(exeption.getMessage())
                .timeStamp(System.currentTimeMillis())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleDeleteException(ResourceDeleteException exeption) {
        ExceptionResponse response = ExceptionResponse.builder()
                .type("/exception/"+ exeption.getClass().getSimpleName())
                .title("Delete resource fail")
                .detail(exeption.getMessage())
                .timeStamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> globalExceptionHandler(Exception ex) {
        ExceptionResponse message = ExceptionResponse.builder()
                .type("Excetion")
                .title("Internal server error")
                .detail(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(System.currentTimeMillis())
                .build();

        return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
