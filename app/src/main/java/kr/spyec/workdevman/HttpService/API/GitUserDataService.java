package kr.spyec.workdevman.HttpService.API;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by qkswk on 2015-12-24.
 */
public interface GitUserDataService {
    @GET("/user")
    Call<GitUserData> loadUserData(@Query("access_token") String accessToken);
}
