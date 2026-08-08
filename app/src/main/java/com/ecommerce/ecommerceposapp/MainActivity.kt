package com.ecommerce.ecommerceposapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ecommerce.ecommerceposapp.presentation.navigation.PosAppRoot
import com.ecommerce.ecommerceposapp.ui.theme.EcommercePosAppTheme
import com.ecommerce.ecommerceposapp.util.PhysicalScannerInput

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommercePosAppTheme {
                PosAppRoot()
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (PhysicalScannerInput.onKeyEvent(event)) return true
        return super.dispatchKeyEvent(event)
    }
}
