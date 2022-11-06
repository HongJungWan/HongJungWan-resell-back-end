package com.resell.resell.domain.users.user;

import com.resell.resell.domain.users.admin.AdminRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, AdminRepository {

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);

    // 이메일, 비밀번호 불일치 체크
    boolean existsByEmailAndPassword(String email, String password);

    void deleteByEmail(String email);

    // 유저 이메일 찾기, 위시리스트용
    @EntityGraph(attributePaths = {"addressBook", "cart"})
    Optional<User> findByEmail(String email);

}
