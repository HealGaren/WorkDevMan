package kr.applepi.workdevman.HttpService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.applepi.workdevman.Define.CDServer;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by qkswk on 2015-12-27.
 */
public class NetServiceFactory {

    private static Retrofit APIRetrofit =
            new Retrofit.Builder()
                    .baseUrl(CDServer.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()
                    ))
                    .build();

    private static Retrofit homeRetrofit =
            new Retrofit.Builder()
                    .baseUrl(CDServer.BASE_HOME_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    public static <C> C createAPIService(Class<C> serviceClass) {
        return APIRetrofit.create(serviceClass);
    }

    public static <C> C createHomeService(Class<C> serviceClass) {
        return homeRetrofit.create(serviceClass);
    }
}
