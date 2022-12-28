package com.resell.resell.domain.users.common;

import com.resell.resell.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED) // @Inheritance 어노테이션으로 어떤 상속 전략을 사용 할 것인지 명시
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn // 부모 클래스에 선언한다. 하위 클래스를 구분하는 용도의 컬럼
public abstract class UserBase extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(unique = true)
    protected String email;

    protected String password;

    @Enumerated(EnumType.STRING)
    protected UserLevel userLevel;
    
}
