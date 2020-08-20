CaptureActivityHandler
    接收到消息后调用restartPreviewAndDecode()
    调用CameraManager.get().requestPreviewFrame(), 传了DecodeHandler和R.id.decode进去
    requestPreviewFrame()中给camera的previewCallback设置了setHandler(handler, message)
    在previewCallback的onPreviewFrame()回调中通过DecodeHandler发送R.id.decode消息。
    DecodeHandler中接收到消息解析数据，并把结果发送给CaptureActivityHandler


while(decode_fail){
    requestPreviewFrame() -> decode ->
                                        decode_success
                                        decode_fail
}