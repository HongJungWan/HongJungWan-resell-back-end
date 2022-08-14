package com.resell.resell.service;

import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.user.DuplicateEmailException;
import com.resell.resell.exception.user.DuplicateNicknameException;
import com.resell.resell.service.encrytion.EncryptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    UserService userService;

    private UserDto.SaveRequest createUserDto() {
        UserDto.SaveRequest saveRequest = UserDto.SaveRequest.builder()
                .email("HongJungWan@test.com")
                .password("test1234")
                .phone("01077777777")
                .nickname("GodHong")
                .build();

        return saveRequest;
    }

    private User createUser() {
        return createUserDto().toEntity();
    }

    @Test
    @DisplayName("이메일 중복 -> 회원 가입 실패")
    void checkEmailDuplicate() {
        UserDto.SaveRequest saveRequest = createUserDto();
        when(userRepository.existsByEmail("HongJungWan@test.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.save(saveRequest));

        verify(userRepository, atLeastOnce()).existsByEmail("HongJungWan@test.com");
    }

    @Test
    @DisplayName("닉네임 중복 -> 회원 가입 실패")
    void checkNicknameDuplicate() {
        UserDto.SaveRequest saveRequest = createUserDto();
        when(userRepository.existsByNickname("GodHong")).thenReturn(true);

        assertThrows(DuplicateNicknameException.class, () -> userService.save(saveRequest));

        verify(userRepository, atLeastOnce()).existsByNickname("GodHong");
    }

}