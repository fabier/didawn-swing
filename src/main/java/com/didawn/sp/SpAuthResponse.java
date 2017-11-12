package com.didawn.sp;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author fabier
 */
public class SpAuthResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("scope")
    private String scope;

    @SerializedName("expires_in")
    private String expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    /**
     *
     */
    public SpAuthResponse() {
    }

    /**
     *
     * @return
     */
    public String getAccessToken() {
	return accessToken;
    }

    /**
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    /**
     *
     * @return
     */
    public String getTokenType() {
	return tokenType;
    }

    /**
     *
     * @param tokenType
     */
    public void setTokenType(String tokenType) {
	this.tokenType = tokenType;
    }

    /**
     *
     * @return
     */
    public String getScope() {
	return scope;
    }

    /**
     *
     * @param scope
     */
    public void setScope(String scope) {
	this.scope = scope;
    }

    /**
     *
     * @return
     */
    public String getExpiresIn() {
	return expiresIn;
    }

    /**
     *
     * @param expiresIn
     */
    public void setExpiresIn(String expiresIn) {
	this.expiresIn = expiresIn;
    }

    /**
     *
     * @return
     */
    public String getRefreshToken() {
	return refreshToken;
    }

    /**
     *
     * @param refreshToken
     */
    public void setRefreshToken(String refreshToken) {
	this.refreshToken = refreshToken;
    }

}
