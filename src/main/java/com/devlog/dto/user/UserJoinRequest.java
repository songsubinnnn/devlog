package com.devlog.dto.user;

import com.devlog.domain.user.User;
import lombok.*;

/**
 * @author sbsong
 * @package com.devlog.dto.user
 * @Classname UserJoinRequest.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-08-21 sbsong : 최초작성
 * </PRE>
 * @since 2025-08-21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;

    public User toEntity(){
        return User.builder()
            .email(email)
            .password(password)
            .role(User.Role.USER) // test용
            .nickname(nickname)
            .build();
    }
}
