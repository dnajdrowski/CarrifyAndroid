package pl.carrifyandroid.Utils;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.carrifyandroid.API.ApiModels.ApiErrorResponse;
import timber.log.Timber;

public class ErrorHandler {

    public static String getMessageFromErrorBody(ResponseBody responseBody) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("500", "Fatal Server Error!");
        if (responseBody != null)
            try {
                apiErrorResponse = new Gson().fromJson(responseBody.string(), ApiErrorResponse.class);
                Timber.d(responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return apiErrorResponse.getMsg();
    }

}
