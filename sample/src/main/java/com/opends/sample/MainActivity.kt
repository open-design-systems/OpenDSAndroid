package com.opends.sample

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.open.design.system.OpenDesignSystemTheme

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    backgroundColor = 0xffFFFFFF,
    showBackground = true
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xff000000,
    showBackground = true
)
fun Test() {
    Column {
        Text(
            color = OpenDesignSystemTheme.color.background,
            text = "Text working"
        )
        Text(
            color = OpenDesignSystemTheme.color.primary,
            text = "Text working"
        )
        Text(
            color = OpenDesignSystemTheme.color.secondary,
            text = "Text working"
        )
    }
}