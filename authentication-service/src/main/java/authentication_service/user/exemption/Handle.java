package authentication_service.user.exemption;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice


public class Handle {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {

        String rootMessage = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        String message = "User with these details already exists";

        if (rootMessage != null) {
            if (rootMessage.contains("UKeamk4l51hm6yqb8xw37i23kb5")) {
                message = "Email already exists";
            } else if (rootMessage.contains("UK1cj1c5umljjjqx8wvju3cjp9j")) {
                message = "Phone number already exists";
            }
        }

        Map<String, String> body = new HashMap<>();
        body.put("message", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}