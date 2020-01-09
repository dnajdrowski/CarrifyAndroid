package pl.carrifyandroid.DI.Component;

import javax.inject.Singleton;

import dagger.Component;
import pl.carrifyandroid.DI.Module.AppModule;
import pl.carrifyandroid.MainActivity;
import pl.carrifyandroid.Screens.Auth.Login.LoginActivity;
import pl.carrifyandroid.Screens.Auth.Register.RegisterActivity;
import pl.carrifyandroid.Screens.CarPreview.CarPreviewDialog;
import pl.carrifyandroid.Screens.DriverLicense.DriverLicenseActivity;
import pl.carrifyandroid.Screens.History.HistoryActivity;
import pl.carrifyandroid.Screens.Maps.MapsFragment;
import pl.carrifyandroid.Screens.SplashScreen.SplashActivity;
import pl.carrifyandroid.Screens.Wallet.WalletActivity;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(CarPreviewDialog carPreviewDialog);

    void inject(MapsFragment mapsFragment);

    void inject(RegisterActivity authActivity);

    void inject(LoginActivity authActivity);

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

    void inject(HistoryActivity historyActivity);

    void inject(DriverLicenseActivity driverLicenseActivity);

    void inject(WalletActivity walletActivity);
}
