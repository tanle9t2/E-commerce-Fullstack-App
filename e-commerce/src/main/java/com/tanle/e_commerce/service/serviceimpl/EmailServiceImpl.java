package com.tanle.e_commerce.service.serviceimpl;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.PaymentDTO;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.exception.ResourceExistedException;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.service.EmailService;
import com.tanle.e_commerce.utils.Helper;
import jakarta.servlet.http.HttpSession;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${spring.mail.from}")
    private String senderEmail;
    @Value("${sendgrid.template-confirm-payment-id}")
    private String templateConfirmPaymentId;
    @Autowired
    private UserRepository userRepository;

    private String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void storeOTP(HttpSession session, String otp) {
        session.setAttribute("otp", otp);
        session.setAttribute("otp_expiry", LocalDateTime.now().plusMinutes(5)); // Expire in 5 minutes
    }

    @Override
    public void sendOtp(String toEmail, HttpSession httpSession) {
        Boolean isExist = userRepository.findByEmail(toEmail)
                .isPresent();
        if (isExist)
            throw new ResourceExistedException("Email existed");
        String subject = "Verify email";
        String otp = generateOTP();
        Email from = new Email(senderEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Your OTP code is: " + otp);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
            storeOTP(httpSession, otp);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean verifyOTP(HttpSession session, String otp) {
        String sessionOtp = (String) session.getAttribute("otp");
        LocalDateTime expiry = (LocalDateTime) session.getAttribute("otp_expiry");

        if (sessionOtp == null || expiry == null || expiry.isBefore(LocalDateTime.now())) {
            return false;
        }
        if (sessionOtp.equals(otp)) {
            session.removeAttribute("otp"); // Remove OTP after successful verification
            session.removeAttribute("otp_expiry");
            return true;
        }
        return false;
    }

    @Override
    public void sendConfirmPayment(String toEmail, PaymentDTO paymentDTO) {
        MyUser myUser = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));


        Email from = new Email(senderEmail);
        Email to = new Email(toEmail);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateConfirmPaymentId);
        Personalization personalization = new Personalization();

        personalization.addTo(to);

        // Add dynamic template data
        JSONObject jsonData = new JSONObject();
        jsonData.put("username", myUser.getUsername());
        jsonData.put("payment_date", paymentDTO.getCreatedAt());
        jsonData.put("products", paymentDTO.getOrder().getItemList());

        personalization.addDynamicTemplateData("orderDate", Helper.convertDatetoString(paymentDTO.getOrder().getCreatedAt()));
        personalization.addDynamicTemplateData("paymentDate", Helper.convertDatetoString(paymentDTO.getCreatedAt()));
        personalization.addDynamicTemplateData("username", myUser.getUsername());
        personalization.addDynamicTemplateData("sellerName", paymentDTO.getOrder().getTenantOrder().getTenantName());
        personalization.addDynamicTemplateData("orderId", paymentDTO.getOrder().getId());
        personalization.addDynamicTemplateData("products", paymentDTO.getOrder().getItemList());
        personalization.addDynamicTemplateData("finalPrice", paymentDTO.getAmount());
        personalization.addDynamicTemplateData("totalPrice", paymentDTO.getOrder().getTotalPrice());
        personalization.addDynamicTemplateData("deliveryFee", 63000);

        mail.addPersonalization(personalization);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
