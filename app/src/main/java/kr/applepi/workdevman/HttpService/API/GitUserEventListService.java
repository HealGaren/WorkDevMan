package kr.applepi.workdevman.HttpService.API;


import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by qkswk on 2015-12-24.
 */
public interface GitUserEventListService {
    @GET("/users/{username}/events")
    Call<List<GitEventData>> loadEventList(
            @Path("username") String username,
            @Query("access_token") String accessToken
    );
}
