package per.wsj.lib.qrcodescanner.request;

import android.graphics.Bitmap;

public interface ScanCallback {

    void onAnalyzeSuccess(Bitmap mBitmap, String result);

    void onAnalyzeFailed();
}
