package kr.applepi.workdevman.HttpService.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qkswk on 2015-12-26.
 */
public class GitRepoData {
    String name;

    @SerializedName("html_url")
    String url;

    String description;

    public GitRepoData(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
