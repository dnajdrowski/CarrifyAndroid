package pl.carrifyandroid.API;

import pl.carrifyandroid.Models.TestUser;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    String url = "http://127.0.0.1:8080/api/";

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @FormUrlEncoded
    @POST("hello")
    Call<TestUser> testMethod(
            @Field("name") String name);

}
