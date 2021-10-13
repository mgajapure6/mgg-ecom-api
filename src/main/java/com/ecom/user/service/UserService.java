package com.ecom.user.service;

import com.ecom.app.payload.ApiResponse;
import com.ecom.app.security.UserPrincipal;
import com.ecom.user.model.User;
import com.ecom.user.payload.InfoRequest;
import com.ecom.user.payload.UserIdentityAvailability;
import com.ecom.user.payload.UserProfile;
import com.ecom.user.payload.UserSummary;

public interface UserService {

	UserSummary getCurrentUser(UserPrincipal currentUser);

	UserIdentityAvailability checkUsernameAvailability(String username);

	UserIdentityAvailability checkEmailAvailability(String email);

	UserProfile getUserProfile(String username);

	User addUser(User user);
	
	User addVendor(User user);

	User updateUser(User newUser, String username, UserPrincipal currentUser);

	ApiResponse deleteUser(String username, UserPrincipal currentUser);

	ApiResponse giveAdmin(String username);
	
	ApiResponse giveVendor(String username);

	ApiResponse removeAdmin(String username);
	
	ApiResponse removeVendor(String username);

	UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);
	
	User verifyUserByAcccountVerificationCode(String code);

}