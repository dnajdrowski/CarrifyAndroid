package pl.carrifyandroid.DI.Component;

import javax.inject.Singleton;

import dagger.Component;
import pl.carrifyandroid.DI.Module.AppModule;
import pl.carrifyandroid.MainActivity;
import pl.carrifyandroid.Screens.SplashScreen.SplashActivity;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

}
