package per.wsj.lib.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import per.wsj.lib.qrcodescanner.request.ScanCallback;


public class ScanActivity extends AppCompatActivity {

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
        if(getActionBar()!=null){
            getActionBar().hide();
        }

        setContentView(R.layout.activity_scan);
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
