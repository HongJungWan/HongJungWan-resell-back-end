package com.resell.resell.service;

import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.user.UserNotFoundException;
import com.resell.resell.service.encrytion.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static com.resell.resell.common.utils.constants.UserConstants.AUTH_STATUS;
import static com.resell.resell.common.utils.constants.UserConstants.USER_ID;
import static com.resell.resell.controller.dto.UserDto.LoginRequest;
import static com.resell.resell.controller.dto.UserDto.UserInfoDto;

@Service
@RequiredArgsConstructor
public class SessionLoginService {

    private final HttpSession session;

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;


    @Transactional(readOnly = true)
    public void existByEmailAndPassword(LoginRequest loginRequest) {
        loginRequest.passwordEncryption(encryptionService);
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        if (!userRepository.existsByEmailAndPassword(email, password)) {
            throw new UserNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public void login(LoginRequest loginRequest) {
        existByEmailAndPassword(loginRequest);
        String email = loginRequest.getEmail();
        setUserLevel(email);
        session.setAttribute(USER_ID, email);
    }

    public void setUserLevel(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        session.setAttribute(AUTH_STATUS, user.getUserLevel());
    }

    public void logout() {
        session.removeAttribute(USER_ID);
    }

    public String getLoginUser() {
        return (String) session.getAttribute(USER_ID);
    }

    public UserLevel getUserLevel() {
        return (UserLevel) session.getAttribute(AUTH_STATUS);
    }

    @Transactional(readOnly = true)
    public UserInfoDto getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다.")).toUserInfoDto();
    }

}
