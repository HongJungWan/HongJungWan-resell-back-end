package com.resell.resell.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 상속된 엔티티에 매핑 정보가 적용되도록 지정한다.
@EntityListeners(AuditingEntityListener.class) // 이벤트가 발생할 때 호출해 줄 Listener 클래스 지정
public abstract class BaseTimeEntity {

    @CreatedDate // 등록 날짜를 나타내는 필드로 선언
    @Column(updatable = false) // 실수로 값을 바꿔도 업데이트되지 않는다.
    private LocalDateTime createdDate;

    @LastModifiedDate // 수정 날짜를 나타내는 필드로 선언
    private LocalDateTime modifiedDate;

}
