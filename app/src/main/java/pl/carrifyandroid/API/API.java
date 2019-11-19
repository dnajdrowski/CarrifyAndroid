package pl.carrifyandroid.API;

import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.API.ApiModels.JwtVerifyTokenRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface API {

    String url = "https://carrify.herokuapp.com/";

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @POST("auth/verifyToken")
    Call<Integer> verifyToken(
            @Body JwtVerifyTokenRequest jwtVerifyTokenRequest,
            @Header("Authorization") String token);

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @POST("auth")
    Call<AuthRequest> loginRequest(
            @Body AuthRequest authRequest);

}
