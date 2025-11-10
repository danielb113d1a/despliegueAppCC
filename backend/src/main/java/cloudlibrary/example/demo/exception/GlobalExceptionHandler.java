package cloudlibrary.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {

        log.warn("Recurso no encontrado: {} (Ruta: {})", ex.getMessage(), request.getDescription(false));

        Map<String, String> errorBody = Map.of(
                "message", ex.getMessage(),
                "path", request.getDescription(false)
        );
        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {

        log.warn("Argumento ilegal: {} (Ruta: {})", ex.getMessage(), request.getDescription(false));

        Map<String, String> errorBody = Map.of(
                "message", ex.getMessage(),
                "path", request.getDescription(false)
        );
        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {

        log.error("Error inesperado en la aplicación: (Ruta: {})", request.getDescription(false), ex);

        Map<String, String> errorBody = Map.of(
                "message", "Error interno del servidor",
                "path", request.getDescription(false)
        );
        return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}