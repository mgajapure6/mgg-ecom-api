package com.ecom.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.app.exceptions.AccessDeniedException;
import com.ecom.app.exceptions.AppException;
import com.ecom.app.exceptions.BadRequestException;
import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.exceptions.UnauthorizedException;
import com.ecom.app.payload.ApiResponse;
import com.ecom.app.security.UserPrincipal;
import com.ecom.user.model.Address;
import com.ecom.user.model.Company;
import com.ecom.user.model.Geo;
import com.ecom.user.model.Role;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.payload.InfoRequest;
import com.ecom.user.payload.UserIdentityAvailability;
import com.ecom.user.payload.UserProfile;
import com.ecom.user.payload.UserSummary;
import com.ecom.user.repository.RoleRepository;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserSummary getCurrentUser(UserPrincipal currentUser) {
		return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
				currentUser.getLastName());
	}

	@Override
	public UserIdentityAvailability checkUsernameAvailability(String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@Override
	public UserIdentityAvailability checkEmailAvailability(String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@Override
	public UserProfile getUserProfile(String username) {
		User user = userRepository.getUserByName(username);

		Long orderCount = 0l;// postRepository.countByCreatedBy(user.getId());

		return new UserProfile(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
				user.getCreatedAt(), user.getEmail(), user.getAddress(), user.getPhone(), user.getWebsite(),
				user.getCompany(), orderCount);
	}

	@Override
	public User addUser(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
			throw new BadRequestException(apiResponse);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
			throw new BadRequestException(apiResponse);
		}

		List<Role> roles = new ArrayList<>();
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User addVendor(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
			throw new BadRequestException(apiResponse);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
			throw new BadRequestException(apiResponse);
		}

		List<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findByName(RoleName.ROLE_VENDOR)
				.orElseThrow(() -> new AppException("Vendor role not set")));
		user.setRoles(roles);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User newUser, String username, UserPrincipal currentUser) {
		User user = userRepository.getUserByName(username);
		if (user.getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			user.setFirstName(newUser.getFirstName());
			user.setLastName(newUser.getLastName());
			user.setPassword(passwordEncoder.encode(newUser.getPassword()));
			user.setAddress(newUser.getAddress());
			user.setPhone(newUser.getPhone());
			user.setWebsite(newUser.getWebsite());
			user.setCompany(newUser.getCompany());

			return userRepository.save(user);

		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
				"You don't have permission to update profile of: " + username);
		throw new UnauthorizedException(apiResponse);

	}

	@Override
	public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", username));
		if (!user.getId().equals(currentUser.getId())
				|| !currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
					"You don't have permission to delete profile of: " + username);
			throw new AccessDeniedException(apiResponse);
		}

		userRepository.deleteById(user.getId());

		return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + username);
	}

	@Override
	public ApiResponse giveAdmin(String username) {
		User user = userRepository.getUserByName(username);
		List<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
				.orElseThrow(() -> new AppException("Admin role not set")));
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);
		userRepository.save(user);
		return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + username);
	}

	@Override
	public ApiResponse giveVendor(String username) {
		User user = userRepository.getUserByName(username);
		List<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findByName(RoleName.ROLE_VENDOR)
				.orElseThrow(() -> new AppException("Vendor role not set")));
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);
		userRepository.save(user);
		return new ApiResponse(Boolean.TRUE, "You gave VENDOR role to user: " + username);
	}

	@Override
	public ApiResponse removeAdmin(String username) {
		User user = userRepository.getUserByName(username);
		List<Role> roles = new ArrayList<>();
		roles.add(
				roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);
		userRepository.save(user);
		return new ApiResponse(Boolean.TRUE, "You took ADMIN role from user: " + username);
	}

	@Override
	public ApiResponse removeVendor(String username) {
		User user = userRepository.getUserByName(username);
		List<Role> roles = new ArrayList<>();
		roles.add(roleRepository.findByName(RoleName.ROLE_VENDOR)
				.orElseThrow(() -> new AppException("User role not set")));
		user.setRoles(roles);
		userRepository.save(user);
		return new ApiResponse(Boolean.TRUE, "You took VENDOR role from user: " + username);
	}

	@Override
	public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
		User user = userRepository.findByUsername(currentUser.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));
		Geo geo = new Geo(infoRequest.getLat(), infoRequest.getLng());
		Address address = new Address(infoRequest.getStreet(), infoRequest.getSuite(), infoRequest.getCity(),
				infoRequest.getZipcode(), geo);
		Company company = new Company(infoRequest.getCompanyName(), infoRequest.getCatchPhrase(), infoRequest.getBs());
		if (user.getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			user.setAddress(address);
			user.setCompany(company);
			user.setWebsite(infoRequest.getWebsite());
			user.setPhone(infoRequest.getPhone());
			User updatedUser = userRepository.save(user);

			Long orderCount = 0l;// postRepository.countByCreatedBy(updatedUser.getId());

			return new UserProfile(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getFirstName(),
					updatedUser.getLastName(), updatedUser.getCreatedAt(), updatedUser.getEmail(),
					updatedUser.getAddress(), updatedUser.getPhone(), updatedUser.getWebsite(),
					updatedUser.getCompany(), orderCount);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile",
				HttpStatus.FORBIDDEN);
		throw new AccessDeniedException(apiResponse);
	}
}
