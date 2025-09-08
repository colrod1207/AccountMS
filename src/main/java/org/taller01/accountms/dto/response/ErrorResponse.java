package org.taller01.AccountMS.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private String message;
  private String error;
  private int status;
  private LocalDateTime timestamp;
  private String path;

  public static ErrorResponse of(String message, String error, int status, String path) {
    return new ErrorResponse(message, error, status, LocalDateTime.now(), path);
  }
}
