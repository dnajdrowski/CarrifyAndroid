package pl.carrifyandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pl.carrifyandroid.Utils.StorageHelper;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Inject
    StorageHelper storageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.component.inject(this);
    }
}
