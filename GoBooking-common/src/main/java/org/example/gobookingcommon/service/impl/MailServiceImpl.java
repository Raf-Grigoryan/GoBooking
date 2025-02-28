package org.example.gobookingcommon.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.request.RoleChangeRequest;
import org.example.gobookingcommon.entity.user.Role;
import org.example.gobookingcommon.entity.user.User;
import org.example.gobookingcommon.repository.BookingRepository;
import org.example.gobookingcommon.repository.CompanyRepository;
import org.example.gobookingcommon.repository.UserRepository;
import org.example.gobookingcommon.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final MailSender mailSender;

    private final JavaMailSender javaMailSender;

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;
    private final BookingRepository bookingRepository;


    @Value("${site.url}")
    private String url;

    @Override
    @Async
    public void sendMailForUserVerify(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to GoBooking Web Site! Please verify your account by clicking on this link ");
            message.setText(url + "/auth/verify?email=" + to + "&token=" + token);

            mailSender.send(message);
            log.info("Verification email sent to {}", to);
        } catch (Exception e) {
            log.error("Error sending verification email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendMailForPromotionRequest(List<String> adminsEmails, String requesterEmail, String context) {
        for (String adminEmail : adminsEmails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("Promotion Request from " + requesterEmail);
            message.setText("Hello, User" + requesterEmail + " has requested a promotion to Director" + " + context + "
                    + context);
            mailSender.send(message);
        }
    }

    @Async
    @Override
    public void sendMailForPromotionRequestAgree(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Promotion Request Agree");
        message.setText("Hello " + to + " your promotion request agree");
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendMailForPromotionRequestDisagree(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Promotion Request Agree");
        message.setText("Hello " + to + " your promotion request disagree");
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendMailForRoleChangeRequest(RoleChangeRequest request) {
        Company company = request.getCompany();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmployee().getEmail());
        message.setSubject("Job Offer at " + company.getName());
        message.setText("We have carefully reviewed your professional background and would like to offer you a position at\n" +
                company.getName() + ". Your experience and skills are a great fit for our team, and we believe we can provide \n" +
                "you with exciting opportunities for growth.\n" +
                "If you're interested in discussing the details, we’d be happy to arrange a call or meeting at a convenient \n" +
                "time for you.\n\n" +
                "Director: " + company.getDirector().getName() + "\n" +
                "Company: " + company.getName() + "\n" +
                "Phone: " + company.getPhone());
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendMailForChangePassword(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password changed");
        message.setText("New password is " + newPassword + " " + to);
        mailSender.send(message);
    }

    @Override
    @Async
    public void sendMailForDeleteCard(String to, String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Card Deleted");
        message.setText("Your Card in " + context + " deleted");
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendMailForRoleChangeRequestAgree(String to, String workerName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Role Change Request from " + workerName);
        message.setText("Your role changed request agree");
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendMailForRoleChangeRequestDisagree(String to, String workerName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Role Change Request from " + workerName);
        message.setText("Your role changed request disagree");
        mailSender.send(message);
    }


    public byte[] generateExcelReport() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");


        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Worker Name");
        headerRow.createCell(1).setCellValue("Monthly Income");


        int rowIndex = 1;
        List<User> directors = userRepository.getUserByRole(Role.DIRECTOR);

        for (User director : directors) {
            Company company = companyRepository.findCompanyByDirector(director);
            if (company != null) {
                List<User> workers = userRepository.findUserByCompany(company);
                for (User worker : workers) {
                    Row dataRow = sheet.createRow(rowIndex++);
                    dataRow.createCell(0).setCellValue(worker.getName());
                    dataRow.createCell(0).setCellValue(bookingRepository.sumTotalEarningsByWorker(worker.getId()));
                }
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }


    public void sendEmailWithAttachment(String to) throws  IOException, jakarta.mail.MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject("Scheduled Email with Excel Attachment");
        helper.setText("Please find the attached Excel report.");


        helper.addAttachment("Report.xlsx", new org.springframework.core.io.ByteArrayResource(generateExcelReport()));

        javaMailSender.send(mimeMessage);
    }



    public void sendScheduledEmail() {
        for (User director : userRepository.getUserByRole(Role.DIRECTOR)) {
            try {
                sendEmailWithAttachment(director.getEmail());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void sendSubscriptionExpiryEmail(String toEmail, String userName, String expiryDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Subscription Expiring Soon!");
        message.setText(
                "Dear " + userName + "," +
                        "Your subscription will expire on " + expiryDate +
                        ". Please renew to continue enjoying our services." +
                        "Best Regards,Your Company");

        mailSender.send(message);
    }

    @Override
    public void sendSubscriptionDeletedEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Subscription Deleted");
        message.setText(
                "Dear " + userName + "," +
                        "Your subscription has been deleted." +
                        "Best Regards,Your Company");
        mailSender.send(message);
    }
}
