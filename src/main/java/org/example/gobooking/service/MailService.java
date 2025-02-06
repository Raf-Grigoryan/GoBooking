package org.example.gobooking.service;


import org.example.gobooking.entity.request.RoleChangeRequest;

import java.util.List;

public interface MailService {

    void sendMailForUserVerify(String to, String token);

    void sendMailForPromotionRequest(List<String> adminsEmails, String requesterEmail, String context);

    void sendMailForPromotionRequestAgree(String to);

    void sendMailForPromotionRequestDisagree(String to);

    void sendMailForRoleChangeRequest(RoleChangeRequest request);


}
