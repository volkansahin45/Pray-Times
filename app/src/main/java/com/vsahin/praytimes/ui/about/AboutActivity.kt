package com.vsahin.praytimes.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.remote.interactions.RemoteActivityHelper
import androidx.wear.widget.ConfirmationOverlay
import com.vsahin.praytimes.R
import com.vsahin.praytimes.databinding.ActivityAboutBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val remoteActivityHelper = RemoteActivityHelper(this)

        binding.buttonGithub.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(Companion.GITHUB_URL))

                    remoteActivityHelper.startRemoteActivity(intent).await()

                    ConfirmationOverlay()
                        .setMessage(getString(R.string.sent_to_your_phone))
                        .setDuration(2000)
                        .showOn(this@AboutActivity)
                } catch (cancellationException: CancellationException) {
                    throw cancellationException
                } catch (throwable: Throwable) {
                    ConfirmationOverlay()
                        .setType(ConfirmationOverlay.FAILURE_ANIMATION)
                        .setMessage(getString(R.string.unknown_error))
                        .showOn(this@AboutActivity)
                }
            }
        }
    }

    companion object {
        private const val GITHUB_URL = "https://github.com/volkansahin45/pray-times"
    }
}