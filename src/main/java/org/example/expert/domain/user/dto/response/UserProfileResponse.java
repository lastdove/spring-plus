package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserProfileResponse {
  private String nickname;

  public UserProfileResponse(String nickname) {
    this.nickname = nickname;
  }
}
