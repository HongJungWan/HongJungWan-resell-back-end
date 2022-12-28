package com.resell.resell.domain.users.admin;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.controller.dto.UserDto.UserListResponse;
import com.resell.resell.controller.dto.UserDto.UserSearchCondition;
import com.resell.resell.domain.users.common.UserLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.resell.resell.domain.users.user.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserListResponse> searchByUsers(UserSearchCondition searchRequest, Pageable pageable) {
        QueryResults<UserListResponse> results = jpaQueryFactory
                .select(Projections.fields(UserListResponse.class,
                        user.id,
                        user.email,
                        user.userLevel))
                .from(user)
                .where(
                        userEmailEq(searchRequest.getEmail()),
                        userIdEq(searchRequest.getId()),
                        userLevelEq(searchRequest.getUserLevel())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<UserListResponse> users = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(users, pageable, total);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression userEmailEq(String userEmail) {
        return hasText(userEmail) ? user.email.endsWith(userEmail) : null;
    }

    private BooleanExpression userLevelEq(UserLevel userLevel) {
        return userLevel != null ? user.userLevel.eq(userLevel) : null;
    }

}