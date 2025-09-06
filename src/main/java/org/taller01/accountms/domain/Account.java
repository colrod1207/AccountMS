package org.taller01.accountms.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

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
