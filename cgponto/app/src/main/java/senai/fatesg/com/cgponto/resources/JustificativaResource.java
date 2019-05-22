package senai.fatesg.com.cgponto.resources;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import senai.fatesg.com.cgponto.model.Justificativa;

public interface JustificativaResource {

    @POST("post")
    Call<Justificativa> post(@Body Justificativa object);
    @GET("get")
    Call<List<Justificativa>> get();

    @GET("get/{id}")
    Call<Justificativa> get(@Path("id") Long id);


    @PUT("put/{id}")
    Call<Justificativa> put(@Body Justificativa object);

    @DELETE("delete/{id}")
    Call<Void> delete(@Body Justificativa object);
}
