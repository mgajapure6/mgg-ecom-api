package com.ecom.user.payload;

import javax.validation.constraints.NotBlank;

public class TokenRequest {

	@NotBlank
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
