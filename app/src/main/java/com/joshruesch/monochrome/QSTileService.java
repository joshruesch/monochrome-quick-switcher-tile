package com.joshruesch.monochrome;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.service.quicksettings.TileService;
import android.widget.Toast;

@SuppressLint("Override")
@TargetApi(Build.VERSION_CODES.N)
public class QSTileService extends TileService{

    @Override
    public void onClick() {
        if (!isSecureSettingsPermGranted(this)) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    /*
                        adb -d shell pm grant com.joshruesch.monochrome android.permission.WRITE_SECURE_SETTINGS
                     */
                    Toast.makeText(QSTileService.this,"Ya'll gonna need permissions", Toast.LENGTH_LONG).show();
                }
            });
           return;
        }

        if (isMonochrome(getContentResolver())) {
            setMonochrome(0, getContentResolver());
        } else {
            setMonochrome(1, getContentResolver());
        }
    }

    public static boolean isSecureSettingsPermGranted(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isMonochrome(ContentResolver contentResolver) {
        return Settings.Secure.getInt(contentResolver, "accessibility_display_daltonizer_enabled", 0) == 1;
    }

    public static void setMonochrome(int value, ContentResolver contentResolver) {
        Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer_enabled", value);
        if (value == 0) {
            Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer", -1);
        } else if (value == 1) {
            Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer", 0);
        }
    }

//    public static void resetMonochrome(ContentResolver contentResolver) {
//        Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer_enabled", 0);
//        Settings.Secure.putInt(contentResolver, "accessibility_display_daltonizer", -1);
//    }

}