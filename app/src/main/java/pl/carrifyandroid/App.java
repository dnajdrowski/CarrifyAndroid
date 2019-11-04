package pl.carrifyandroid;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.squareup.otto.Bus;

import pl.carrifyandroid.DI.Component.AppComponent;
import pl.carrifyandroid.DI.Component.DaggerAppComponent;
import pl.carrifyandroid.DI.Module.AppModule;
import pl.carrifyandroid.Utils.ReleaseTree;
import timber.log.Timber;

public class App extends MultiDexApplication {


    public static AppComponent component;
    private static App instance;

    Bus bus;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        instance = this;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

