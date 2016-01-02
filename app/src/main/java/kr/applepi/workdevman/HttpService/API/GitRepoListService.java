package kr.applepi.workdevman.HttpService.API;


import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by qkswk on 2015-12-24.
 */
public interface GitRepoListService {
    @GET("/user/repos")
    Call<List<GitRepoData>> loadRepoList(
            @Query("access_token") String accessToken,
            @Query("sort") String sort
    );
}
