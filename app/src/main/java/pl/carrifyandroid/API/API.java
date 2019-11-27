package pl.carrifyandroid.API;

import java.util.List;

import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.API.ApiModels.JwtVerifyTokenRequest;
import pl.carrifyandroid.Models.Car;
import pl.carrifyandroid.Models.RegionZone;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {

    String url = "https://carrify.herokuapp.com/";

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @POST("auth/verifyToken")
    Call<Integer> verifyToken(@Body JwtVerifyTokenRequest jwtVerifyTokenRequest);

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @POST("auth")
    Call<AuthRequest> loginRequest(
            @Body AuthRequest authRequest);

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @GET("api/region-zones/{id}")
    Call<RegionZone> getRegionZones(
            @Path("id") int id,
            @Header("Authorization") String token);

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @GET("api/cars")
    Call<List<Car>> getCarsData(
            @Header("Authorization") String token);

}
