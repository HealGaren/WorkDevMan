package kr.applepi.workdevman.HttpService.OAuth;

import kr.applepi.workdevman.Define.CDServer;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by qkswk on 2015-12-27.
 */
public interface GitTokenService {
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")


    Call<GitTokenData> loadTokenData(
            @Field("client_id")
            String clientID,

            @Field("client_secret")
            String clientSecret,

            @Field("code")
            String code
    );
}
