package pl.carrifyandroid.DI.Module;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.carrifyandroid.API.API;
import pl.carrifyandroid.API.RestClient;
import pl.carrifyandroid.Screens.SplashScreen.SplashManager;
import pl.carrifyandroid.Utils.StorageHelper;
import retrofit2.Retrofit;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Context context) {
        return new RestClient(context).provideRetrofit();
    }

    @Provides
    API provideApi(Retrofit retrofit) {
        return retrofit.create(API.class);
    }

    @Singleton
    @Provides
    Bus provideBus() {
        return new Bus();
    }

    @Provides
    StorageHelper provideStorageHelper(Context context) {
        return new StorageHelper(context);
    }

    @Singleton
    @Provides
    SplashManager provideSplashManager(API api, StorageHelper storageHelper) {
        return new SplashManager(api, storageHelper);
    }

}
