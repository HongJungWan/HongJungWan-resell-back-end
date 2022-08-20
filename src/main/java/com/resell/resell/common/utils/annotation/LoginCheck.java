package com.resell.resell.common.utils.annotation;

import com.resell.resell.domain.users.common.UserLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @LoginCheck :  현재 사용자가 로그인 한 사용자인지 확인한다.
 * @Retention : 어느 시점까지 어노테이션의 메모리를 가져갈 지 설정
 * @Target : 어노테이션이 사용될 위치를 지정한다.
 */

@Retention(RUNTIME)
@Target(METHOD)
public @interface LoginCheck {
    UserLevel authority() default UserLevel.UNAUTH;
}
