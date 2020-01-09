package pl.carrifyandroid.API;

import java.util.List;

import okhttp3.MultipartBody;
import pl.carrifyandroid.API.ApiModels.AuthRequest;
import pl.carrifyandroid.API.ApiModels.JwtVerifyTokenRequest;
import pl.carrifyandroid.API.ApiModels.NewRentRequest;
import pl.carrifyandroid.Models.Car;
import pl.carrifyandroid.Models.RegionZone;
import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.Models.UploadResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @POST("api/rents/new-rent")
    Call<Rent> addNewRent(
            @Body NewRentRequest newRentRequest,
            @Header("Authorization") String token);

    /**
     * POST
     * EXAMPLE
     *
     * @return
     */
    @GET("api/rents/user/{id}/active")
    Call<Rent> getActiveRents(
            @Path("id") int id,
            @Header("Authorization") String token);

    /**
     * GET
     * Method which we use to end our rent.
     *
     * @param id    - current rent id
     * @param token - user validation
     * @return Rent object
     */
    @GET("api/rents/{id}/finish")
    Call<Rent> endRent(
            @Path("id") int id,
            @Header("Authorization") String token);

    /**
     * GET
     * Method which give us list of our rents of all time.
     *
     * @param userId - current user id
     * @param token  - user validation
     * @return List of rent objects
     */
    @GET("api/rents/user/{id}/all")
    Call<List<Rent>> getRentHistory(
            @Path("id") int userId,
            @Header("Authorization") String token);

    @Multipart
    @POST("api/driver-licences/user/{id}/upload")
    Call<Integer> uploadDriverLicensePhotos(
            @Path("id") int id,
            @Part MultipartBody.Part front,
            @Part MultipartBody.Part reverse,
            @Header("Authorization") String token);

    @GET("api/driver-licences/user/{id}/check")
    Call<Integer> checkDriverLicense(
            @Path("id") int userId,
            @Header("Authorization") String token);
}
