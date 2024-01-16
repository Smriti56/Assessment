package com.example.assessment.service;


import com.example.assessment.domain.Payment;
import com.example.assessment.domain.UserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    private final JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void notifyUser(Payment payment, UserInformation userInformation){
        logger.info("Sending mail to userId: {} ", userInformation.getUserId());

        SimpleMailMessage emailNotification = new SimpleMailMessage();
        emailNotification.setText("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Payment Notification</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>Payment Notification</h2>\n" +
                "    <p>Hello User,</p>\n" +
                "    \n" +
                "    <p>We are writing to inform you about a recent payment transaction:</p>\n" +
                "    \n" +
                "    <ul>\n" +
                "        <li><strong>User ID:</strong> "+ userInformation.getUserId() + "</li>\n" +
                "        <li><strong>Amount:</strong> "+ payment.getPaymentAmount() + "</li>\n" +
                "        <li><strong>Transaction Date:</strong> "+ payment.getTransactionDate() + "</li>\n" +
                "        <li><strong>Transaction Method:</strong> "+ payment.getPaymentMethod() + "</li>\n" +
                "    </ul>\n" +
                "    \n" +
                "    <p>Thank you for using our payment services!</p>\n" +
                "    \n" +
                "    <p>Best regards,<br>\n" +
                "</body>\n" +
                "</html>\n");
        emailNotification.setSubject("Your receipt for payment on " + payment.getTransactionDate());
        emailNotification.setTo(userInformation.getUserEmail());
        //javaMailSender.send(emailNotification);
    }
}
