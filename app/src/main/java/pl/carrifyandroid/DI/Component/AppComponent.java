package pl.carrifyandroid.DI.Component;

import javax.inject.Singleton;

import dagger.Component;
import pl.carrifyandroid.DI.Module.AppModule;
import pl.carrifyandroid.MainActivity;
import pl.carrifyandroid.Screens.Auth.Login.LoginActivity;
import pl.carrifyandroid.Screens.Auth.Register.RegisterActivity;
import pl.carrifyandroid.Screens.SplashScreen.SplashActivity;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(RegisterActivity authActivity);

    void inject(LoginActivity authActivity);

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

}
