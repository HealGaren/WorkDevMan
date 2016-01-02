package kr.applepi.workdevman.HttpService.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qkswk on 2015-12-24.
 */
public class GitUserData implements Serializable {

    @SerializedName("login")
    String username;

    @SerializedName("avatar_url")
    String imageUrl;

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
