package com.menu.autododge;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

/**
 * MainActivity
 * ------------
 * Pide el permiso de overlay y arranca el servicio.
 * La Activity puede cerrarse; el overlay sigue visible.
 */
public class MainActivity extends Activity {

    private static final int REQ_OVERLAY = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Pedir permiso SYSTEM_ALERT_WINDOW
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQ_OVERLAY);
        } else {
            launchOverlay();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_OVERLAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                launchOverlay();
            } else {
                Toast.makeText(this, "Permiso de overlay denegado.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void launchOverlay() {
        startService(new Intent(this, MenuOverlayService.class));
        // La Activity se cierra; el overlay flotante queda activo
        finish();
    }
}
