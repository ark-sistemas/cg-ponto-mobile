package senai.fatesg.com.cgponto.bootstrap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private final static String ENDPOINT = "http://192.168.43.56:8089";

    public static Retrofit getClient(){

        Retrofit retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT.concat("/justificativa/"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build();

        return retrofit;
    }
}
