package org.taller01.accountms.domain;

import java.math.BigDecimal;
import lombok.*;
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
  private BigDecimal balance;
  private Boolean active;
  private AccountType type;
}
