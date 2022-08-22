package com.resell.resell.common.utils.certification.email;

import static com.resell.resell.common.utils.certification.email.EmailConstants.DOMAIN_NAME;

public class EmailContentTemplate {

    public String buildCertificationContent(String certificationNumber) {

        StringBuilder builder = new StringBuilder();
        builder.append("[ReSell] 인증번호는 ");
        builder.append(certificationNumber);
        builder.append("입니다. ");

        return builder.toString();
    }

    public String buildEmailCheckContent(String token, String email) {
        StringBuilder builder = new StringBuilder();
        builder.append(DOMAIN_NAME);
        builder.append("/users/email-check-token?token=");
        builder.append(token);
        builder.append("&email=");
        builder.append(email);
        return builder.toString();
    }
}
