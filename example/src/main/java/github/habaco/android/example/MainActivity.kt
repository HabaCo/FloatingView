package github.habaco.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import github.habaco.android.view.floating.FloatingUtil
import github.habaco.android.view.floating.FloatingView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var notification: FloatingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonShow?.setOnClickListener {
            notification?.show()
        }

        buttonHide?.setOnClickListener {
            notification?.hide()
        }
    }

    override fun onResume() {
        super.onResume()

        val grandPermission =
            FloatingUtil.needToGrantOverlaysPermission(
                this
            ) {
                AlertDialog.Builder(this)
                    .setTitle("權限要求")
                    .setMessage("請給予 " + resources.getString(R.string.app_name) + " 上層繪製權限以開啟懸浮視窗通知")
                    .setPositiveButton("確認") { dialog, _ ->
                        dialog.dismiss()

                        FloatingUtil.startSetting(this)
                    }.setNegativeButton("離開") { dialog, _ ->
                        dialog.dismiss()
                    }.setCancelable(false)
                    .show()
            }

        if (!grandPermission) {
            notification = FloatingView.Builder(this)
                .title("Hello")
                .message("world")
                .icon(R.mipmap.ic_launcher)
                .onShow {
                    Toast.makeText(this, "onShow", Toast.LENGTH_SHORT).show()
                }
                .onHide {
                    Toast.makeText(this, "onHide", Toast.LENGTH_SHORT).show()
                }
                .onClick {
                    Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show()
                }
                .build()
        }
    }
}