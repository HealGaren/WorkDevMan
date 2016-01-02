package kr.applepi.workdevman.HttpService.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

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

    public long getPushDateLocalTime() {
        long lv_localDateTime = pushDate.getTime();

        TimeZone z = TimeZone.getDefault();
        int offset = z.getOffset(lv_localDateTime); // The offset includes daylight savings time

        lv_localDateTime = lv_localDateTime + offset;

        return lv_localDateTime;

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
