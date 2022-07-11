package di;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import javax.inject.Inject;

/**
*TODO : explain this class
*/

public class PermissionChecker {
    @NonNull
    private final Application application;

    @Inject
    public PermissionChecker(@NonNull Application application) {
        this.application = application;
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(application, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }
}