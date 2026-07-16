package com.ecommerce.ecommerceposapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ecommerce.ecommerceposapp.presentation.navigation.PosAppRoot
import com.ecommerce.ecommerceposapp.ui.theme.EcommercePosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommercePosAppTheme {
                PosAppRoot()
            }
        }
    }
}
