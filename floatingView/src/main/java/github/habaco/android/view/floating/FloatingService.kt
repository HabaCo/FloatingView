package github.habaco.android.view.floating

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager

class FloatingService: Service() {

    private val windowManager: WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onBind(intent: Intent?): IBinder? = null


}