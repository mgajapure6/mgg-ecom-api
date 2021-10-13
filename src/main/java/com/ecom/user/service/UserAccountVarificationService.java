package com.ecom.user.service;

import com.ecom.user.model.User;

public interface UserAccountVarificationService {
	
	String getVerificationCode(User user);
	
	User verifyUserAccount(String verificationCode);
	
	void sendAccountVerificationEmail(User user);

}
