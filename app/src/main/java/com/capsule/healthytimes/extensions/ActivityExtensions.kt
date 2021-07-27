package com.capsule.healthytimes.extensions

import android.app.Activity
import android.content.Intent
import com.capsule.healthytimes.core.CoreComponentProvider

fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")


fun Activity.share(title: String, extraText: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "$title\n$extraText")
        putExtra(Intent.EXTRA_SUBJECT, title)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}