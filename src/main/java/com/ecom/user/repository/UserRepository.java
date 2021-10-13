package com.ecom.user.repository;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.security.UserPrincipal;
import com.ecom.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(@NotBlank String username);

	Optional<User> findByEmail(@NotBlank String email);

	Boolean existsByUsername(@NotBlank String username);

	Boolean existsByEmail(@NotBlank String email);
	
	Optional<User> findByAcccountVerificationCode(@NotBlank String acccountVerificationCode);

	Optional<User> findByUsernameOrEmail(String username, String email);

	default User getUser(UserPrincipal currentUser) {
		return getUserByName(currentUser.getUsername());
	}

	default User getUserByName(String username) {
		return findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}
}
