package com.resell.resell.domain.brand;

import com.resell.resell.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Brand extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nameKor;

    @Column(unique = true)
    private String nameEng;

    private String originImagePath;

    private String thumbnailImagePath;

}
