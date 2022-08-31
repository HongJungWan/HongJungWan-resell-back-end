package com.resell.resell.controller;

import com.resell.resell.common.utils.annotation.CurrentUser;
import com.resell.resell.common.utils.annotation.LoginCheck;
import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.service.EmailCertificationService.EmailCertificationService;
import com.resell.resell.service.SessionLoginService;
import com.resell.resell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.resell.resell.common.utils.constants.ResponseConstants.CREATED;
import static com.resell.resell.controller.dto.UserDto.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserApiController {

    private final UserService userService;
    private final SessionLoginService sessionLoginService;
    private final EmailCertificationService emailCertificationService;

    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplicate(nickname));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto.SaveRequest requestDto) {
        userService.save(requestDto);
        emailCertificationService.sendEmailForEmailCheck(requestDto.getEmail());

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

    @PostMapping("/resend-email-token")
    public void resendEmailCheck(@CurrentUser String email) {
        emailCertificationService.sendEmailForEmailCheck(email);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<UserInfoDto> myPage(@CurrentUser String email) {
        UserInfoDto loginUser = sessionLoginService.getCurrentUser(email);
        return ResponseEntity.ok(loginUser);
    }

    @GetMapping("/find/{email}")
    public ResponseEntity<FindUserResponse> findUser(@PathVariable String email) {
        FindUserResponse findUserResponse = userService.getUserResource(email);
        return ResponseEntity.ok(findUserResponse);
    }

    @PostMapping("/email-certification/sends")
    public ResponseEntity sendEmail(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.sendEmailForCertification(requestDto.getEmail());
        return CREATED;
    }

    @PostMapping("/email-certification/confirms")
    public void emailVerification(@RequestBody EmailCertificationRequest requestDto) {
        emailCertificationService.verifyEmail(requestDto);
    }

    @PatchMapping("/forget/password")
    public void changePasswordByForget(
            @Valid @RequestBody ChangePasswordRequest requestDto) {
        userService.updatePasswordByForget(requestDto);
    }

    @LoginCheck
    @PatchMapping("/password")
    public void changePassword(@CurrentUser String email, @Valid @RequestBody ChangePasswordRequest requestDto) {
        userService.updatePassword(email, requestDto);
    }
    
}
