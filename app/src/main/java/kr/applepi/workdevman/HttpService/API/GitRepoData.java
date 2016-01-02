package kr.applepi.workdevman.HttpService.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qkswk on 2015-12-26.
 */
public class GitRepoData {
    String name;

    @SerializedName("html_url")
    String url;

    String description;


    @SerializedName("pushed_at")
    Date pushDate;

    public long getPushDateTime(){
        return pushDate.getTime();
    }

    public GitRepoData(String name, String url, String description, Date pushDate) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.pushDate = pushDate;
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

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }
}
