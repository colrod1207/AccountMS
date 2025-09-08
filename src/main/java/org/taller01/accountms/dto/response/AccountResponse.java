package org.taller01.accountms.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.taller01.accountms.domain.Account;
import org.taller01.accountms.domain.AccountType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
  private String id;
  private String accountNumber;
  private BigDecimal balance;
  private AccountType type;
  private String clientId;
  private Boolean active;

  public static AccountResponse from(Account account) {
    return new AccountResponse(account.getId(), account.getAccountNumber(), account.getBalance(),
        account.getType(), account.getClientId(), account.getActive());
  }
}
