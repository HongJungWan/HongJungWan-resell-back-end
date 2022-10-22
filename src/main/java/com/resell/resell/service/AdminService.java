package com.resell.resell.service;

import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.domain.users.common.UserStatus;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDto.UserListResponse> findUsers(UserDto.UserSearchCondition requestDto, Pageable pageable) {
        return userRepository.searchByUsers(requestDto, pageable);
    }

    @Transactional(readOnly = true)
    public UserDto.UserDetailsResponse getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));
        return user.toUserDetailsDto();
    }

    @Transactional
    public void updateBanUsers(UserDto.UserBanRequest requestDto) {
        Long id = requestDto.getId();
        UserStatus userStatus = requestDto.getUserStatus();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));
        user.updateUserStatus(userStatus);
    }

}