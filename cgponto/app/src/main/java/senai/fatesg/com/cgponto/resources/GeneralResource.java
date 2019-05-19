package senai.fatesg.com.cgponto.resources;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GeneralResource<T> {

    @GET("/get")
    Call<List<T>> get();

    @GET("/get/{registro}/{id}")
    Call<T> get(@Path("id") Long id);

    @POST("/post")
    Call<T> post(@Body T object);

    @PUT("/put/{id}")
    Call<T> put(@Body T object);

    @DELETE("/delete/{id}")
    Call<Void> delete(@Body T object);

}
