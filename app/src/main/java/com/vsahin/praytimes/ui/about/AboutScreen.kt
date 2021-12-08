package com.vsahin.praytimes.ui.about

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.vsahin.praytimes.R
import com.vsahin.praytimes.common.startRemoteActivity

@Preview
@Composable
fun AboutScreen() {
    val activity = LocalContext.current as Activity
    val scope = rememberCoroutineScope()
    val githubUrl = stringResource(id = R.string.github_url)
    val personalUrl = stringResource(id = R.string.personal_url)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(onClick = { startRemoteActivity(activity, scope, Uri.parse(personalUrl)) }) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.about_info)
            )
        }

        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = { startRemoteActivity(activity, scope, Uri.parse(githubUrl)) }) {
            Icon(
                painter = painterResource(id = R.drawable.github_logo),
                contentDescription = stringResource(
                    id = R.string.github_logo_description
                )
            )
        }
    }
}