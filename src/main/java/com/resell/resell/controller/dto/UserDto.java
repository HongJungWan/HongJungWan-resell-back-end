package com.resell.resell.controller.dto;

import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.domain.users.common.UserStatus;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.service.encrytion.EncryptionService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveRequest {

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
        private String nickname;

        @NotBlank(message = "이메일 주소를 입력해주세요.")
        @Email(message = "올바른 이메일 주소를 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        private String password;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
        private String phone;

        @Builder
        public SaveRequest(String nickname, String email, String password, String phone) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
            this.phone = phone;
        }

        public void passwordEncryption(EncryptionService encryptionService) {
            this.password = encryptionService.encrypt(password);
        }

        public User toEntity() {

            return User.builder()
                    .nickname(this.nickname)
                    .email(this.email)
                    .password(this.password)
                    .nicknameModifiedDate(LocalDateTime.now())
                    .phone(this.phone)
                    .userLevel(UserLevel.ADMIN)
                    .userStatus(UserStatus.NORMAL)
                    .point(0L)
                    .build();
        }
        
    }

}
