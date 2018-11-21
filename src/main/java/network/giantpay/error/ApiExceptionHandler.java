package network.giantpay.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(Exception exception, WebRequest request) throws JsonProcessingException {
        ApiException apiException = (ApiException) exception;

        Map<String, Object> body = new HashMap<>();
        body.put("message", apiException.getMessage());

        if (!Strings.isNullOrEmpty(apiException.getField())) {
            body.put("field", apiException.getField());
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (apiException.getCode() == 400) {
            status = HttpStatus.BAD_REQUEST;
        } else if (apiException.getCode() == 401) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (apiException.getCode() == 403) {
            status = HttpStatus.FORBIDDEN;
        } else if (apiException.getCode() == 404) {
            status = HttpStatus.NOT_FOUND;
        }
        return handleExceptionInternal(apiException, jsonMapper.writeValueAsString(body), new HttpHeaders(), status, request);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    protected String onAuthFailure() {
        return "error";
    }
}
