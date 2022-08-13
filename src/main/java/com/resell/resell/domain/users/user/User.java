package com.resell.resell.domain.users.user;

import com.resell.resell.domain.users.common.Account;
import com.resell.resell.domain.users.common.UserBase;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.domain.users.common.UserStatus;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends UserBase {

    private String nickname;

    private String phone;

    @Embedded // 값 타입을 사용하는 곳에 표시
    private Account account;

    private Long point;

    private LocalDateTime nicknameModifiedDate;

    private UserStatus userStatus;

    @Builder
    public User(Long id, String email, String password, UserLevel userLevel, String nickname, String phone, LocalDateTime nicknameModifiedDate, UserStatus userStatus, Long point) {

        super(id, email, password, userLevel);
        this.nickname = nickname;
        this.phone = phone;
        this.userLevel = userLevel;
        this.nicknameModifiedDate = nicknameModifiedDate;
        this.userStatus = userStatus;
        this.point = point;
    }

}
