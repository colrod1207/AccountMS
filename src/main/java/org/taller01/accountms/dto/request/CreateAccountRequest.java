package org.taller01.AccountMS.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.taller01.AccountMS.domain.AccountType;

public record CreateAccountRequest(@NotBlank(message="Client ID is required")String clientId,

@NotNull(message="Account type is required")AccountType type,

@NotNull(message="Initial balance is required")@PositiveOrZero(message="Initial balance must be zero or positive")BigDecimal initialBalance){}
