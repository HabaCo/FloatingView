package github.habaco.android.view.floating

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

class FloatingUtil {
    companion object {

        /**
         * only when api level > M (23) need to grant this
         *
         * otherwise just need to include [android.permission.SYSTEM_ALERT_WINDOW] in AndroidManifest,
         * and this will always return false
         */
        fun needToGrantOverlaysPermission(context: Context, callbackIfNeed: () -> Unit =  {}): Boolean {
            var canDrawOverlays = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                canDrawOverlays = Settings.canDrawOverlays(context)
                if (!canDrawOverlays)
                {
                    callbackIfNeed()
                }
            }

            return !canDrawOverlays
        }

        /**
         * only when api level > M (23) need to enable this
         */
        @TargetApi(Build.VERSION_CODES.M)
        fun startSetting(context: Context) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivity(intent)
        }

        /**
         * get status bar height in pixels
         */
        fun getStatusBarHeight(context: Context): Int {
            var height = 0

            with(context.resources.getIdentifier("status_bar_height", "dimen", "android")) {
                if (this > 0) {
                    height = context.resources.getDimensionPixelSize(this)
                }
            }

            return height
        }
    }
}