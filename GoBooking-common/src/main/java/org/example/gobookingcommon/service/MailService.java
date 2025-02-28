package org.example.gobookingcommon.service;




import org.example.gobookingcommon.entity.request.RoleChangeRequest;

import javax.mail.MessagingException;
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

    void sendEmailWithAttachment(String to) throws MessagingException, IOException, jakarta.mail.MessagingException;

    void sendScheduledEmail();

    byte[] generateExcelReport() throws IOException;

    void sendSubscriptionExpiryEmail(String toEmail, String userName, String expiryDate);

    void sendSubscriptionDeletedEmail(String toEmail, String userName);
}
