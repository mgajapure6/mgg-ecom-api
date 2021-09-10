package com.ecom.user.payload;

public class JwtAuthenticationResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private String fullToken;

	public JwtAuthenticationResponse(String accessToken) {
		this.accessToken = accessToken;
		this.fullToken = this.tokenType + " " + accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getFullToken() {
		return fullToken;
	}

	public void setFullToken(String fullToken) {
		this.fullToken = fullToken;
	}

}
