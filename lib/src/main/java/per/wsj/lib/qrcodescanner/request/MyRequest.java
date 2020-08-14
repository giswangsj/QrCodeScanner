package per.wsj.lib.qrcodescanner.request;

import android.content.Context;
import android.graphics.Bitmap;

import per.wsj.lib.qrcodescanner.ScanActivity;


public class MyRequest implements IRequest, ScanCallback {

    private Context mContext;

    private Action successAction;
    private Action failAction;

    public MyRequest(Context context) {
        this.mContext = context;
    }

    @Override
    public IRequest onSuccess(Action action) {
        this.successAction = action;
        return this;
    }

    @Override
    public IRequest onFail(Action action) {
        this.failAction = action;
        return this;
    }

    @Override
    public void start() {
        ScanActivity.startScan(mContext, this);
    }

    @Override
    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
        if(successAction!=null){
            successAction.onAction(result);
        }
    }

    @Override
    public void onAnalyzeFailed() {
        if(failAction!=null){
            failAction.onAction("扫描失败,请重试");
        }
    }
}
