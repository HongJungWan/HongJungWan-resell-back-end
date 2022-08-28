package com.resell.resell.domain.users.user;

import com.resell.resell.controller.dto.UserDto.FindUserResponse;
import com.resell.resell.domain.addressBook.Address;
import com.resell.resell.domain.addressBook.AddressBook;
import com.resell.resell.domain.users.common.Account;
import com.resell.resell.domain.users.common.UserBase;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.domain.users.common.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.resell.resell.controller.dto.UserDto.UserInfoDto;

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

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "ADDRESSBOOK_ID")
    private AddressBook addressBook;

    public void updateUserLevel() {
        this.userLevel = UserLevel.AUTH;
    }

    public UserInfoDto toUserInfoDto() {
        return UserInfoDto.builder()
                .email(this.getEmail())
                .nickname(this.getNickname())
                .phone(this.getPhone())
                .userLevel(this.userLevel)
                .build();
    }

    public FindUserResponse toFindUserDto() {
        return FindUserResponse.builder()
                .email(this.getEmail())
                .phone(this.getPhone())
                .build();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateAccount(Account account) {
        this.account = account;
    }

    public void addAddress(Address address) {
        this.addressBook.addAddress(address);
    }

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

    public void createAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public void deleteAddress(Address address) {
        this.addressBook.deleteAddress(address);
    }

}
