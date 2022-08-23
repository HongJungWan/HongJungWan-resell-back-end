package com.resell.resell.service.EmailCertificationService;

import com.resell.resell.common.utils.certification.email.EmailContentTemplate;
import com.resell.resell.common.utils.properties.AppProperties;
import com.resell.resell.dao.EmailCertificationDao;
import com.resell.resell.exception.user.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.resell.resell.common.utils.certification.RandomNumberGeneration.makeRandomNumber;
import static com.resell.resell.common.utils.certification.email.EmailConstants.TITLE_CERTIFICATION;
import static com.resell.resell.common.utils.certification.email.EmailConstants.TITLE_EMAIL_CHECK;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailCertificationService {

    private final EmailCertificationDao emailVerificationDao;
    private final JavaMailSender mailSender;
    private final EmailCertificationDao emailCertificationNumberDao;
    private final AppProperties appProperties;

    // 인증 이메일 내용 생성
    public String makeEmailContent(String certificationNumber) {
        EmailContentTemplate content = new EmailContentTemplate();
        return content.buildCertificationContent(certificationNumber);
    }

    //이메일 확인용 내용 생성
    public String makeEmailContent(String token, String email) {
        EmailContentTemplate content = new EmailContentTemplate();
        return content.buildEmailCheckContent(token, email);
    }

    // 이메일 전송 및 인증번호 저장
    public void sendEmailForCertification(String email) {

        String randomNumber = makeRandomNumber();
        String content = makeEmailContent(randomNumber);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(appProperties.getEmailFromAddress());
        message.setSubject(TITLE_CERTIFICATION);
        message.setText(content);
        mailSender.send(message);

        emailCertificationNumberDao.createEmail(email, randomNumber);
    }

    public void sendEmailForEmailCheck(String email) {

        String token = UUID.randomUUID().toString();
        String content = makeEmailContent(token, email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(appProperties.getEmailFromAddress());
        message.setSubject(TITLE_EMAIL_CHECK);
        message.setText(content);
        mailSender.send(message);

        emailVerificationDao.createEmail(email, token);
    }

    public void verifyEmail(String token, String email) {
        if (isVerify(token, email)) {
            throw new TokenExpiredException("인증 토큰이 만료되었습니다.");
        }
        emailVerificationDao.removeEmailCertification(email);
    }

    private boolean isVerify(String token, String email) {
        return !(emailVerificationDao.hasKey(email) &&
                emailVerificationDao.getEmailCertification(email)
                        .equals(token));
    }

}
