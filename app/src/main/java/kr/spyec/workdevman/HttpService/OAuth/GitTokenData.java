package kr.spyec.workdevman.HttpService.OAuth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qkswk on 2015-12-27.
 */
public class GitTokenData {

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("token_type")
    String tokenType;

    public GitTokenData(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
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
}
