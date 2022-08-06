package com.resell.resell.domain.users.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * 은행명
 * 계좌 번호
 * 예금주
 */

@Embeddable // 값 타입을 정의하는 곳에 표시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 객체 생성에 대해 한번 더 체크
@AllArgsConstructor
public class Account {

    private String bankName;
    private String accountNumber;
    private String depositor;
}
