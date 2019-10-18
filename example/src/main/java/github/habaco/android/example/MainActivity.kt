package github.habaco.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
            FloatingUtil.needToGrantOverlaysPermission(this) {
                AlertDialog.Builder(this)
                    .setTitle("Permission require")
                    .setMessage("Please enable \"Allow display over other apps\" to use floating window normally.")
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()

                        FloatingUtil.startSetting(this)
                    }.setNegativeButton(android.R.string.cancel) { dialog, _ ->
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