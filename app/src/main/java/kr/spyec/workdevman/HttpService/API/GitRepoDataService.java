package kr.spyec.workdevman.HttpService.API;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by qkswk on 2015-12-24.
 */
public interface GitRepoDataService {
    @GET("/repos/{username}/{repo}")
    Call<GitRepoData> loadRepoData(
            @Path("username") String username,
            @Path("repo") String repoName,
            @Query("access_token") String accessToken
    );
}
