package com.vsahin.praytimes.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.wear.remote.interactions.RemoteActivityHelper
import androidx.wear.widget.ConfirmationOverlay
import com.vsahin.praytimes.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

fun startRemoteActivity(
    activity: Activity,
    scope: CoroutineScope,
    data: Uri
) {
    val remoteActivityHelper = RemoteActivityHelper(activity)
    val sentToYourPhoneMessage = activity.getString(R.string.sent_to_your_phone)
    val unknownErrorMessage = activity.getString(R.string.unknown_error)

    scope.launch {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(data)

            remoteActivityHelper.startRemoteActivity(intent).await()

            ConfirmationOverlay()
                .setMessage(sentToYourPhoneMessage)
                .setDuration(2000)
                .showOn(activity)
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (throwable: Throwable) {
            ConfirmationOverlay()
                .setType(ConfirmationOverlay.FAILURE_ANIMATION)
                .setMessage(unknownErrorMessage)
                .showOn(activity)
        }
    }
}