package com.resell.resell.domain.users.admin;

import com.resell.resell.domain.users.common.UserBase;
import com.resell.resell.domain.users.common.UserLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends UserBase {

    @Builder
    public Admin(String email, String password, UserLevel userLevel) {
        this.email = email;
        this.password = password;
        this.userLevel = userLevel;
    }
    
}
