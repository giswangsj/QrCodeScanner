package per.wsj.lib.qrcodescanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import per.wsj.lib.qrcodescanner.request.ScanCallback;


public class ScanActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1 << 2;

    private static ScanCallback mScanCallback;

    public static void startScan(Context context, ScanCallback scanCallback) {
        mScanCallback = scanCallback;
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_scan);

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            startScan();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                startScan();
            } else {
                Toast.makeText(this, "请授予请求权限", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void startScan() {
        ScanFragment scanFragment = new ScanFragment();
        scanFragment.setScanCallback(mScanCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, scanFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanCallback = null;
    }
}
