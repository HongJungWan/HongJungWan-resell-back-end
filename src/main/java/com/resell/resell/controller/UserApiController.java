package com.resell.resell.controller;

import com.resell.resell.common.utils.annotation.CurrentUser;
import com.resell.resell.common.utils.annotation.LoginCheck;
import com.resell.resell.service.SessionLoginService;
import com.resell.resell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.resell.resell.common.utils.constants.ResponseConstants.CREATED;
import static com.resell.resell.controller.dto.UserDto.LoginRequest;
import static com.resell.resell.controller.dto.UserDto.SaveRequest;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserApiController {

    private final UserService userService;

    private final SessionLoginService sessionLoginService;

    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplicate(nickname));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody SaveRequest requestDto) {
        userService.save(requestDto);
        return CREATED;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        sessionLoginService.login(loginRequest);
    }

    @LoginCheck
    @DeleteMapping("/logout")
    public void logout(@CurrentUser String email) {
        sessionLoginService.logout();
    }

    @GetMapping("/email-check-token")
    public void emailCheck(String token, String email) {
        userService.updateEmailVerified(token, email);
    }

}
