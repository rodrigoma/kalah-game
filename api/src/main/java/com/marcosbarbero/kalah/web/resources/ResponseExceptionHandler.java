package com.marcosbarbero.kalah.web.resources;

import com.marcosbarbero.kalah.dto.error.ErrorResourceDTO;
import com.marcosbarbero.kalah.dto.error.FieldErrorResourceDTO;
import com.marcosbarbero.kalah.exception.GameFinishedException;
import com.marcosbarbero.kalah.exception.InvalidRequestException;
import com.marcosbarbero.kalah.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.RequestDispatcher;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcos Barbero
 */
@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler {

    private static final int SCOPE_REQUEST = RequestAttributes.SCOPE_REQUEST;

    private ErrorResourceDTO errorResource(String code, String message) {
        return new ErrorResourceDTO(code, message);
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    private ResponseEntity<ErrorResourceDTO> sendResponse(ErrorResourceDTO error, HttpStatus status) {
        return new ResponseEntity<>(error, headers(), status);
    }

    @ExceptionHandler(InvalidRequestException.class)
    protected ResponseEntity<ErrorResourceDTO> handleInvalidRequest(InvalidRequestException exception) {
        List<FieldErrorResourceDTO> fieldErrorResources = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getErrors().getFieldErrors();
        fieldErrors.forEach(fieldError -> fieldErrorResources.add(FieldErrorResourceDTO.build(fieldError)));
        ErrorResourceDTO error = errorResource("InvalidRequest", exception.getMessage());
        error.setFieldErrors(fieldErrorResources);
        log.warn("[Invalid Request] Error: {}", error.toString());
        return sendResponse(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorResourceDTO> handleResourceNotFound(Exception exception) {
        ErrorResourceDTO error = errorResource("ResourceNotFound", exception.getMessage());
        log.warn("[Resource NOT_FOUND] Error: {}", error.toString());
        return sendResponse(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameFinishedException.class)
    protected ResponseEntity<ErrorResourceDTO> handleGameFinished(GameFinishedException exception) {
        ErrorResourceDTO error = errorResource("GameFinished", exception.getMessage());
        log.info("Game Finished: {}", error.toString());
        return sendResponse(error, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResourceDTO> globalExceptionHandler(RuntimeException e, WebRequest request) {
        HttpStatus httpStatus = getHttpStatus(getErrorStatus(request));
        ErrorResourceDTO error = errorResource(e.getMessage(), getErrorMessage(request, httpStatus));
        log.warn("[Unexpected exception] Error: {}", e);
        return sendResponse(error, httpStatus);
    }

    private String getErrorMessage(WebRequest request, HttpStatus httpStatus) {
        Throwable exc = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION, SCOPE_REQUEST);
        return exc != null ? exc.getMessage() : httpStatus.getReasonPhrase();
    }

    private int getErrorStatus(WebRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE, SCOPE_REQUEST);
        return statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private HttpStatus getHttpStatus(int statusCode) {
        return HttpStatus.valueOf(statusCode);
    }
}
