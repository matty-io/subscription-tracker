package com.subscriptiontracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Entity
@Getter
@NoArgsConstructor
public class VerificationCode  {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String code;
  @Setter
  private boolean emailSent = false;

  @OneToOne
  private User user;

  public VerificationCode(User user) {
    this.user = user;
    this.code = RandomStringUtils.random(6, false, true);
  }
}
