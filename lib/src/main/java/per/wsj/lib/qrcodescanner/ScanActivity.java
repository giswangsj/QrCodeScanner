package per.wsj.lib.qrcodescanner;

import android.Manifest;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import per.wsj.lib.qrcodescanner.request.ScanCallback;


public class ScanActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PER_CAMERA = 1 << 2;
    private static final int REQUEST_CODE_PER_ALBUM = 1 << 3;
    private static final int REQUEST_CODE_ALBUM = 1 << 4;

    private static ScanCallback mScanCallback;

    private ImageView ivLight;

    private ImageView ivPic;

    private boolean isOpenLight = true;

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PER_CAMERA);
        }
        // 闪光灯
        ivLight = findViewById(R.id.ivLight);
        ivLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QrCodeScanner.isLightEnable(isOpenLight);
                isOpenLight = !isOpenLight;
            }
        });

        // 相册选择图片
        ivPic = findViewById(R.id.ivPic);
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        ScanActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    openAlbum();
                } else {
                    ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PER_ALBUM);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PER_CAMERA) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                startScan();
            } else {
                Toast.makeText(this, "请授予相机权限", Toast.LENGTH_LONG).show();
                finish();
            }
        } else if (requestCode == REQUEST_CODE_PER_ALBUM) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                Toast.makeText(this, "请授予读取磁盘权限", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 开始扫描
     */
    private void startScan() {
        ScanFragment scanFragment = new ScanFragment();
        scanFragment.setScanCallback(mScanCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, scanFragment).commit();
    }

    /**
     * 调用相册或文件管理器，选择图片
     */
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALBUM) {
            if (data != null && data.getData() != null) {
                QrCodeScanner.analyzeBitmap(getPathFromUri(data.getData()), new ScanCallback() {

                    @Override
                    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                        if (mScanCallback != null) {
                            mScanCallback.onAnalyzeSuccess(mBitmap, result);
                        }
                        ScanActivity.this.finish();
                    }

                    @Override
                    public void onAnalyzeFailed() {
                        if (mScanCallback != null) {
                            mScanCallback.onAnalyzeFailed();
                        }
                        ScanActivity.this.finish();
                    }
                });
            }
        }
    }

    /**
     *
     * @param uri
     * @return
     */
    public String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,projection,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanCallback = null;
    }
}
