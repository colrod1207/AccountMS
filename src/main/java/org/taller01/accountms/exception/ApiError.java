package org.taller01.accountms.exception;

import java.time.Instant;
import java.util.Map;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiError {
    private Instant timestamp;
    private int status;
    private String error;      // "No encontrado", "Conflicto", etc.
    private String message;    // Mensaje claro para el usuario
    private String path;       // URI
    private Map<String,String> fieldErrors; // opcional para validaciones
}
