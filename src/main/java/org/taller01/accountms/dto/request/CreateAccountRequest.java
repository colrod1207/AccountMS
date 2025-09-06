package org.taller01.accountms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank String customerId,
        @NotBlank String number,
        @NotBlank String currency,
        @NotNull @PositiveOrZero BigDecimal initialBalance
) {}
