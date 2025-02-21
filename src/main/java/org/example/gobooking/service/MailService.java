package org.example.gobooking.service;


import jakarta.mail.MessagingException;
import org.example.gobooking.entity.request.RoleChangeRequest;

import java.io.IOException;
import java.util.List;

public interface MailService {

    void sendMailForUserVerify(String to, String token);

    void sendMailForPromotionRequest(List<String> adminsEmails, String requesterEmail, String context);

    void sendMailForPromotionRequestAgree(String to);

    void sendMailForPromotionRequestDisagree(String to);

    void sendMailForRoleChangeRequest(RoleChangeRequest request);

    void sendMailForChangePassword(String to, String newPassword);

    void sendMailForDeleteCard(String to,String context);

    void sendMailForRoleChangeRequestAgree(String to, String workerName);

    void sendMailForRoleChangeRequestDisagree(String to, String workerName);

    void sendEmailWithAttachment(String to) throws MessagingException, IOException;

    void sendScheduledEmail();

    byte[] generateExcelReport() throws IOException;

    void sendSubscriptionExpiryEmail(String toEmail, String userName, String expiryDate);

    void sendSubscriptionDeletedEmail(String toEmail, String userName);
}
