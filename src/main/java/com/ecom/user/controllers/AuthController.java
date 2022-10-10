package com.ecom.user.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.app.exceptions.AppException;
import com.ecom.app.exceptions.EcommApiException;
import com.ecom.app.exceptions.UnauthorizedException;
import com.ecom.app.payload.ApiResponse;
import com.ecom.app.security.JwtTokenProvider;
import com.ecom.user.model.Role;
import com.ecom.user.model.RoleName;
import com.ecom.user.model.User;
import com.ecom.user.payload.JwtAuthenticationResponse;
import com.ecom.user.payload.LoginRequest;
import com.ecom.user.payload.SignUpRequest;
import com.ecom.user.payload.TokenRequest;
import com.ecom.user.repository.RoleRepository;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.service.CustomUserDetailsService;
import com.ecom.user.service.UserAccountVarificationService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication API", description = "")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final String USER_ROLE_NOT_SET = "User role not set";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserAccountVarificationService userAccountVarificationService;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@PostMapping("/signin")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtTokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}
	
	@PostMapping("/signin-with-token")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUserWithToken(@Valid @RequestBody TokenRequest tokenRequest) {
		String jwt = tokenRequest.getAccessToken();
		if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
			String username = jwtTokenProvider.getUsernameFromJWT(jwt);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			String newJwt = jwtTokenProvider.generateToken(authenticationToken);
			return ResponseEntity.ok(new JwtAuthenticationResponse(newJwt));
		}
		throw new UnauthorizedException("Invalid Token");
		
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
			throw new EcommApiException(HttpStatus.BAD_REQUEST, "Username is already taken");
		}

		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			throw new EcommApiException(HttpStatus.BAD_REQUEST, "Email is already taken");
		}

		String firstName = signUpRequest.getFirstName().toLowerCase();

		String lastName = signUpRequest.getLastName().toLowerCase();

		String username = signUpRequest.getUsername().toLowerCase();

		String email = signUpRequest.getEmail().toLowerCase();

		String password = passwordEncoder.encode(signUpRequest.getPassword());

		User user = new User(firstName, lastName, username, email, password);
		

		List<Role> roles = new ArrayList<>();

		if (userRepository.count() == 0) {
			roles.add(roleRepository.findByName(RoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
			roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
			roles.add(roleRepository.findByName(RoleName.ROLE_VENDOR)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
		} else {
			roles.add(roleRepository.findByName(RoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
		}

		user.setRoles(roles);
		
		user.setActive(true);
		user.setAccountVerified(false);
		user.setAcccountVerificationCode(userAccountVarificationService.getVerificationCode(user));

		User newUser = userRepository.save(user);
		
		userAccountVarificationService.sendAccountVerificationEmail(newUser);

//		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{userId}")
//				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "User registered successfully"));
	}
}
