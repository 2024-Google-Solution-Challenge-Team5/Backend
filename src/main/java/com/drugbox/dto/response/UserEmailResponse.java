package com.drugbox.dto.response;

import com.drugbox.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailResponse {
    private String email;

    public static UserEmailResponse of(User user) {
        return new UserEmailResponse(user.getEmail());
    }
}
