package org.taller01.accountms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.taller01.accountms.domain.AccountType;public record CreateAccountRequest(@NotBlank String clientId,@NotNull @PositiveOrZero BigDecimal initialBalance,@NotNull AccountType accountType){}
