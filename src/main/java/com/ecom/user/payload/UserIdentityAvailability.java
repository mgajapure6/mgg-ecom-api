package com.ecom.user.payload;

public class UserIdentityAvailability {
	private Boolean available;

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public UserIdentityAvailability(Boolean available) {
		super();
		this.available = available;
	}

}
