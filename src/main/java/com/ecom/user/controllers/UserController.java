package com.ecom.user.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.app.payload.ApiResponse;
import com.ecom.app.security.CurrentUser;
import com.ecom.app.security.UserPrincipal;
import com.ecom.user.model.User;
import com.ecom.user.payload.InfoRequest;
import com.ecom.user.payload.UserIdentityAvailability;
import com.ecom.user.payload.UserProfile;
import com.ecom.user.payload.UserSummary;
import com.ecom.user.service.UserAccountVarificationService;
import com.ecom.user.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User's API", description = "")
@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAccountVarificationService userAccountVarificationService;

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER') or hasRole('VENDOR')")
	public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = userService.getCurrentUser(currentUser);

		return new ResponseEntity<>(userSummary, HttpStatus.OK);
	}

	@GetMapping("/checkUsernameAvailability")
	public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(
			@RequestParam(value = "username") String username) {
		UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(username);

		return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
	}

	@GetMapping("/checkEmailAvailability")
	public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(
			@RequestParam(value = "email") String email) {
		UserIdentityAvailability userIdentityAvailability = userService.checkEmailAvailability(email);
		return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
	}

	@GetMapping("/{username}/profile")
	public ResponseEntity<UserProfile> getUSerProfile(@PathVariable(value = "username") String username) {
		UserProfile userProfile = userService.getUserProfile(username);

		return new ResponseEntity<>(userProfile, HttpStatus.OK);
	}

	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		user.setActive(true);
		user.setAccountVerified(false);
		user.setAcccountVerificationCode(userAccountVarificationService.getVerificationCode(user));
		User newUser = userService.addUser(user);
		userAccountVarificationService.sendAccountVerificationEmail(newUser);
		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	@PutMapping("/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('VENDOR')")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser,
			@PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser) {
		User updatedUSer = userService.updateUser(newUser, username, currentUser);

		return new ResponseEntity<>(updatedUSer, HttpStatus.CREATED);
	}

	@DeleteMapping("/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('VENDOR')")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = userService.deleteUser(username, currentUser);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/{username}/giveAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userService.giveAdmin(username);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@PutMapping("/{username}/giveVendor")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> giveVendor(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userService.giveVendor(username);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/{username}/takeAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> takeAdmin(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userService.removeAdmin(username);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@PutMapping("/{username}/takeVendor")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> takeVendor(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userService.removeVendor(username);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/setOrUpdateInfo")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('VENDOR')")
	public ResponseEntity<UserProfile> setAddress(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody InfoRequest infoRequest) {
		UserProfile userProfile = userService.setOrUpdateInfo(currentUser, infoRequest);

		return new ResponseEntity<>(userProfile, HttpStatus.OK);
	}
	
	@GetMapping("/accVerify")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> verifyUserAccount(@RequestParam(name = "code") String code) {
		userService.verifyUserByAcccountVerificationCode(code);
		return new ResponseEntity<>("User verified successfully", HttpStatus.OK);
	}

}
