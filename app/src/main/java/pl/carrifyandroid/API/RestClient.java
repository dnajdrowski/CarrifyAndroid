package pl.carrifyandroid.API;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.carrifyandroid.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    Context context;

    public RestClient(Context context) {
        this.context = context;
    }

    public Retrofit provideRetrofit() {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(chain -> {
                    Request request = chain.request();
                    Request newRequest = request.newBuilder()
                            .addHeader("Content-type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                })
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(Protocol.HTTP_1_1);
        protocols.add(Protocol.HTTP_2);
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(API.url);

        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(client);
        return builder.build();
    }

}
