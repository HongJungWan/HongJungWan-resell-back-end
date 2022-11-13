package com.resell.resell.service;

import com.resell.resell.domain.addressBook.AddressRepository;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.user.DuplicateEmailException;
import com.resell.resell.exception.user.DuplicateNicknameException;
import com.resell.resell.exception.user.UserNotFoundException;
import com.resell.resell.service.encrytion.EncryptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.resell.resell.controller.dto.UserDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @ExtendWith : Junit5의 확장 어노테이션을 사용할 수 있다.
 * @Mock : mock 객체를 생성한다.
 * @InjectMock : @Mock이 붙은 객체를 @InjectMock이 붙은 객체에 주입시킬 수 있다.
 */

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    EncryptionService encryptionService;

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    UserService userService;

    private SaveRequest createUserDto() {
        SaveRequest saveRequest = SaveRequest.builder()
                .email("hong43ok@gmail.com")
                .password("test1234")
                .phone("01077777777")
                .nickname("hong43ok")
                .build();

        return saveRequest;
    }

    private User createUser() {
        return createUserDto().toEntity();
    }

    @Test
    @DisplayName("이메일 중복 -> 회원 가입 실패")
    void checkEmailDuplicate() {
        SaveRequest saveRequest = createUserDto();
        when(userRepository.existsByEmail("hong43ok@gmail.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.save(saveRequest));

        verify(userRepository, atLeastOnce()).existsByEmail("hong43ok@gmail.com");
    }

    @Test
    @DisplayName("닉네임 중복 -> 회원 가입 실패")
    void checkNicknameDuplicate() {
        SaveRequest saveRequest = createUserDto();
        when(userRepository.existsByNickname("hong43ok")).thenReturn(true);

        assertThrows(DuplicateNicknameException.class, () -> userService.save(saveRequest));

        verify(userRepository, atLeastOnce()).existsByNickname("hong43ok");
    }

    @Test
    @DisplayName("비밀번호 찾기 -> 전달받은 객체(이메일)가 회원이면 -> 비밀번호 변경 성공")
    public void updatePasswordByForget() {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .email("hong43ok@gmail.com")
                .passwordAfter("test12345")
                .build();
        User user = createUserDto().toEntity();

        when(userRepository.findByEmail(changePasswordRequest.getEmail()))
                .thenReturn(Optional.of(user));

        userService.updatePasswordByForget(changePasswordRequest);

        assertThat(user.getPassword()).isEqualTo(changePasswordRequest.getPasswordAfter());

        verify(userRepository, atLeastOnce()).findByEmail(changePasswordRequest.getEmail());
    }

    @Test
    @DisplayName("[가입 O] 이메일 입력 -> 비밀번호 변경에 필요한 리소스 리턴")
    public void SuccessToGetUserResource() {
        String email = "hong43ok@gmail.com";
        User user = createUserDto().toEntity();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        FindUserResponse userResource = userService.getUserResource(email);

        assertThat(userResource.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResource.getPhone()).isEqualTo(user.getPhone());
    }

    @Test
    @DisplayName("[가입 X] 이메일 입력 -> 비밀번호 변경에 필요한 리소스 리턴 실패")
    public void failToGetUserResource() {

        String email = "not@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserResource("not@test.com"));

        verify(userRepository, atLeastOnce()).findByEmail(email);
    }

    @Test
    @DisplayName("비밀번호 변경 -> 이전 비밀번호와 일치 시 -> 비밀번호 변경 성공")
    public void updatePassword() {
        User user = createUserDto().toEntity();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest
                .builder()
                .email(null)
                .passwordBefore("test1234")
                .passwordAfter("test12345")
                .build();

        String email = "hong43ok@gmail.com";
        String passwordBefore = encryptionService.encrypt(changePasswordRequest.getPasswordBefore());
        String passwordAfter = encryptionService.encrypt(changePasswordRequest.getPasswordAfter());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndPassword(email, passwordBefore)).thenReturn(true);

        userService.updatePassword(email, changePasswordRequest);

        assertThat(user.getPassword()).isEqualTo(passwordAfter);

        verify(userRepository, atLeastOnce()).findByEmail(email);
        verify(userRepository, atLeastOnce()).existsByEmailAndPassword(email, passwordBefore);
    }

}