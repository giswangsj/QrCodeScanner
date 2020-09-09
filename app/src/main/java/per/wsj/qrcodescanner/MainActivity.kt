package per.wsj.qrcodescanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import per.wsj.lib.qrcodescanner.QrCodeScanner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            QrCodeScanner.with(this)
                .onSuccess {
                    tvResult.text = "$it"
                }
                .onFail {
                    tvResult.text = "扫描失败：$it"
                }
                .start()
        }


        btnGenerate.setOnClickListener {
            if (etContent.text.toString().isNotBlank()) {
                ivQrCode.setImageBitmap(
                    QrCodeScanner.createQrCode(
                        etContent.text.toString(),
                        500,
                        500,
                        null
                    )
                )
            }
        }
    }
}