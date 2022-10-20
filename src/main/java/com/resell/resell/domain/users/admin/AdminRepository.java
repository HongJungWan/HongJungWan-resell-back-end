package com.resell.resell.domain.users.admin;

import com.resell.resell.controller.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminRepository {

    Page<UserDto.UserListResponse> searchByUsers(UserDto.UserSearchCondition searchRequest, Pageable pageable);
}
