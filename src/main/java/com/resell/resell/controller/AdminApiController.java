package com.resell.resell.controller;

import com.resell.resell.common.annotation.LoginCheck;
import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.domain.users.common.UserLevel;
import com.resell.resell.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;

    @LoginCheck(authority = UserLevel.ADMIN)
    @GetMapping("/users")
    public Page<UserDto.UserListResponse> findByUsers(UserDto.UserSearchCondition requestDto, Pageable pageable) {
        return adminService.findUsers(requestDto, pageable);
    }

    @LoginCheck(authority = UserLevel.ADMIN)
    @GetMapping("/users/{id}")
    public UserDto.UserDetailsResponse getUserDetails(@PathVariable Long id) {
        return adminService.getUser(id);
    }

    @LoginCheck(authority = UserLevel.ADMIN)
    @PostMapping("/users/ban")
    public void restrictUsers(@RequestBody UserDto.UserBanRequest requestDto) {
        adminService.updateBanUsers(requestDto);
    }

}