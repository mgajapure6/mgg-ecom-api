package com.ecom.user.dto;

import javax.validation.constraints.NotBlank;

public class UserDTO {

	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String LastName;

	@NotBlank
	private String username;

	@NotBlank
	private String email;

	public UserDTO(Long id, @NotBlank String firstName, @NotBlank String lastName, @NotBlank String username,
			@NotBlank String email) {
		super();
		this.id = id;
		this.firstName = firstName;
		LastName = lastName;
		this.username = username;
		this.email = email;
	}

	public UserDTO(Long id) {
		this.id = id;
	}

	public UserDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
