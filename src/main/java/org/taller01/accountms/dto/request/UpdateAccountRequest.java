package org.taller01.accountms.dto.request;

import jakarta.validation.constraints.Pattern;

public record UpdateAccountRequest(@Pattern(regexp="PEN|USD|EUR|GBP")String currency,Boolean active){}
