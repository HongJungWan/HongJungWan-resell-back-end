package com.resell.resell.controller;

import com.resell.resell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.resell.resell.common.utils.constants.ResponseConstants.CREATED;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/user-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @GetMapping("/user-nicknames/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplicate(nickname));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody com.resell.resell.controller.dto.UserDto.SaveRequest requestDto) {
        userService.save(requestDto);
        return CREATED;
    }

}
