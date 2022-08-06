package com.resell.resell.domain.users.user;

import com.resell.resell.domain.users.common.Account;
import com.resell.resell.domain.users.common.UserBase;
import com.resell.resell.domain.users.common.UserStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
