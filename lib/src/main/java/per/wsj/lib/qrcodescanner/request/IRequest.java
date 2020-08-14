package per.wsj.lib.qrcodescanner.request;

public interface IRequest {

    IRequest onSuccess(Action action);

    IRequest onFail(Action action);

    void start();
}
