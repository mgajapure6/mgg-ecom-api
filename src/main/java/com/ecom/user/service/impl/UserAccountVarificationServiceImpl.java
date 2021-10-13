package com.ecom.user.service.impl;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ecom.app.exceptions.EcommApiException;
import com.ecom.user.model.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.service.UserAccountVarificationService;

@Service
public class UserAccountVarificationServiceImpl implements UserAccountVarificationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender emailSender;
	
	@Value("${app.baseURL}")
	private String appBaseUrl;
	
	@Value("${spring.mail.username}")
	private String appFromEmail;

	@Override
	public String getVerificationCode(User user) {
		return RandomStringUtils.random(25, user.getEmail());
	}

	@Override
	public User verifyUserAccount(String verificationCode) {
		User user = userRepository.findByAcccountVerificationCode(verificationCode)
				.orElseThrow(() -> new EcommApiException(HttpStatus.BAD_REQUEST, "Invalid verification code"));
		user.setAccountVerified(true);
		user = userRepository.save(user);
		return user;
	}

	@Override
	public void sendAccountVerificationEmail(User user) {
		String toAddress = user.getEmail();
		String senderName = "Mgg Ecommerce Company";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Mgg Ecommerce Company.";

		MimeMessage message;
		try {
			message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(appFromEmail, senderName);
			helper.setTo(toAddress);
			helper.setSubject(subject);

			content = content.replace("[[name]]", user.getFirstName()+" "+user.getLastName());
			String verifyURL = appBaseUrl + "/api/users/accVerify?code=" + user.getAcccountVerificationCode();

			content = content.replace("[[URL]]", verifyURL);

			helper.setText(content, true);
			
			emailSender.send(message);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
