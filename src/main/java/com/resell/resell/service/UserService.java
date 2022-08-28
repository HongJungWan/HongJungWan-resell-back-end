package com.resell.resell.service;

import com.resell.resell.controller.dto.UserDto;
import com.resell.resell.domain.addressBook.Address;
import com.resell.resell.domain.addressBook.AddressBook;
import com.resell.resell.domain.addressBook.AddressBookRepository;
import com.resell.resell.domain.addressBook.AddressRepository;
import com.resell.resell.domain.users.common.Account;
import com.resell.resell.domain.users.user.User;
import com.resell.resell.domain.users.user.UserRepository;
import com.resell.resell.exception.user.DuplicateEmailException;
import com.resell.resell.exception.user.DuplicateNicknameException;
import com.resell.resell.exception.user.UnauthenticatedUserException;
import com.resell.resell.exception.user.UserNotFoundException;
import com.resell.resell.service.EmailCertificationService.EmailCertificationService;
import com.resell.resell.service.encrytion.EncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.resell.resell.controller.dto.AddressDto.IdRequest;
import static com.resell.resell.controller.dto.AddressDto.SaveRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final EmailCertificationService emailCertificationService;
    private final AddressBookRepository addressBookRepository;
    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void save(com.resell.resell.controller.dto.UserDto.SaveRequest requestDto) {
        if (checkEmailDuplicate(requestDto.getEmail())) {
            throw new DuplicateEmailException();
        }
        if (checkNicknameDuplicate(requestDto.getNickname())) {
            throw new DuplicateNicknameException();
        }
        requestDto.passwordEncryption(encryptionService);

        User user = userRepository.save(requestDto.toEntity());
    }

    private void validToken(String token, String email) {
        emailCertificationService.verifyEmail(token, email);
    }

    @Transactional
    public void updateEmailVerified(String token, String email) {
        validToken(token, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));
        user.updateUserLevel();
    }

    @Transactional(readOnly = true)
    public UserDto.FindUserResponse getUserResource(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 email 입니다.")).toFindUserDto();
    }

    @Transactional
    public void updatePasswordByForget(UserDto.ChangePasswordRequest requestDto) {
        String email = requestDto.getEmail();
        requestDto.passwordEncryption(encryptionService);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        user.updatePassword(requestDto.getPasswordAfter());
    }

    @Transactional
    public void updatePassword(String email, UserDto.ChangePasswordRequest requestDto) {
        requestDto.passwordEncryption(encryptionService);
        String passwordBefore = requestDto.getPasswordBefore();
        String passwordAfter = requestDto.getPasswordAfter();
        if (!userRepository.existsByEmailAndPassword(email, passwordBefore)) {
            throw new UnauthenticatedUserException("이전 비밀번호가 일치하지 않습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        user.updatePassword(passwordAfter);
    }

    @Transactional(readOnly = true)
    public Account getAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        return user.getAccount();
    }

    @Transactional
    public void updateAccount(String email, Account account) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));
        user.updateAccount(account);
    }

    @Transactional(readOnly = true)
    public List<Address> getAddressBook(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        AddressBook addressBook = user.getAddressBook();
        return addressBook.getAddressList();

    }

    @Transactional
    public void addAddress(String email, SaveRequest requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        if (user.getAddressBook() == null) {
            AddressBook addressBook = addressBookRepository.save(new AddressBook());
            user.createAddressBook(addressBook);
        }

        user.addAddress(requestDto.toEntity());
    }

    @Transactional
    public void deleteAddress(String email, IdRequest idRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자 입니다."));

        Long addressId = idRequest.getId();

        Address address = addressRepository.findById(addressId).orElseThrow();

        user.deleteAddress(address);

    }

    @Transactional
    public void updateAddress(SaveRequest requestDto) {

        Long addressId = requestDto.getId();
        Address address = addressRepository.findById(addressId).orElseThrow();
        address.updateAddress(requestDto);

    }

}
