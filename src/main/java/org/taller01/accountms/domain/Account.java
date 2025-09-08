package org.taller01.AccountMS.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
  @Id
  private String id;
  private String clientId;
  @Indexed(unique = true)
  private String accountNumber;
  private String currency;
  private BigDecimal balance;
  private Boolean active;
  private AccountType type;
}
