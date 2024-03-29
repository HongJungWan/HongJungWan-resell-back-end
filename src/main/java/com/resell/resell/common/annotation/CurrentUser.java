package com.resell.resell.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @CurrentUser : 현재 로그인된 USER의 email(아이디)를 가져온다.
 * @Retention : 어느 시점까지 어노테이션의 메모리를 가져갈 지 설정
 * @Target : 어노테이션이 사용될 위치를 지정한다.
 */

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface CurrentUser {
    
}
