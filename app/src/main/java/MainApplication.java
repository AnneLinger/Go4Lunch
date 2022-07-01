import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
*MainApplication for Hilt
*/

@HiltAndroidApp
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
