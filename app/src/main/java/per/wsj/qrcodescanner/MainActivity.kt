package per.wsj.qrcodescanner

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import per.wsj.lib.qrcodescanner.QrCodeScanner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            QrCodeScanner.with(this)
                .onSuccess {
                    tvResult.text = "扫描结果：$it"
                }
                .onFail {
                    tvResult.text ="扫描失败：$it"
                }
                .start()
        }
    }
}