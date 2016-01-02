package kr.applepi.workdevman.HttpService.API;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by qkswk on 2016-01-01.
 */
public class GitEventData {

    String type;

    @SerializedName("created_at")
    Date createdDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public GitEventData(String type, Date createdDate) {

        this.type = type;
        this.createdDate = createdDate;
    }

    public boolean isPushEvent(){
        return getType().equals("PushEvent");
    }
}

